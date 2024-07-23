package com.rj.poc.graphs.util


import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.renderer.BarChartRenderer
import com.github.mikephil.charting.utils.ViewPortHandler

class RoundedBarChartRenderer(
    chart: BarChart,
    animator: ChartAnimator,
    viewPortHandler: ViewPortHandler
) : BarChartRenderer(chart, animator, viewPortHandler) {

    private val radius = 20f

    override fun drawDataSet(c: Canvas?, dataSet: IBarDataSet?, index: Int) {
        val trans = mChart.getTransformer(dataSet?.axisDependency)

        mBarBorderPaint.color = dataSet!!.barBorderColor
        mBarBorderPaint.strokeWidth = dataSet.barBorderWidth

        val phaseX = mAnimator.phaseX
        val phaseY = mAnimator.phaseY

        if (mBarBuffers.size < index + 1) return

        val buffer = mBarBuffers[index]
        buffer.setPhases(phaseX, phaseY)
        buffer.setDataSet(index)
        buffer.setInverted(mChart.isInverted(dataSet.axisDependency))
        buffer.setBarWidth(mChart.barData.barWidth)

        buffer.feed(dataSet)

        trans.pointValuesToPixel(buffer.buffer)

        for (j in 0 until buffer.size() step 4) {
            if (!mViewPortHandler.isInBoundsLeft(buffer.buffer[j + 2])) continue
            if (!mViewPortHandler.isInBoundsRight(buffer.buffer[j])) break

            val barRect = RectF(buffer.buffer[j], buffer.buffer[j + 1], buffer.buffer[j + 2], buffer.buffer[j + 3])
            val path = Path().apply {
                addRoundRect(barRect, radius, radius, Path.Direction.CW)
            }

            mRenderPaint.color = dataSet.color
            c?.drawPath(path, mRenderPaint)

            if (dataSet.barBorderWidth > 0f) {
                c?.drawPath(path, mBarBorderPaint)
            }
        }
    }

    override fun drawHighlighted(c: Canvas, indices: Array<Highlight>) {
        val barData = mChart.barData

        for (highlight in indices) {
            val set = barData.getDataSetByIndex(highlight.dataSetIndex)
            if (set == null || !set.isHighlightEnabled) continue

            val e = set.getEntryForXValue(highlight.x, highlight.y)

            if (!isInBoundsX(e, set)) continue

            val trans = mChart.getTransformer(set.axisDependency)

            mHighlightPaint.color = set.highLightColor
            mHighlightPaint.alpha = set.highLightAlpha

            val barWidth = mChart.barData.barWidth
            val x = e.x
            val y = e.y

            val barRect = RectF(
                x - barWidth / 2f,
                if (y >= 0) y else 0f,
                x + barWidth / 2f,
                if (y <= 0) y else 0f
            )

            trans.rectValueToPixel(barRect)

            val path = Path().apply {
                addRoundRect(barRect, radius, radius, Path.Direction.CW)
            }

            c.drawPath(path, mHighlightPaint)
        }
    }

}
