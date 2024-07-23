package com.rj.poc.graphs

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.rj.poc.graphs.util.RoundedBarChartRenderer

class BarChartActivity : AppCompatActivity() {

    // Sample data (replace with your actual data)
    val breathValues = listOf(60f, 120f, 180f, 210f, 150f, 90f, 60f)
    val relaxValues =  listOf(40f, 60f, 90f, 105f, 75f, 60f, 40f)
    val focusValues =  listOf(20f, 30f, 45f, 55f, 33f, 30f, 20f)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bar_chart)

        val barChart = findViewById<BarChart>(R.id.barGraph)
        createBarChart(barChart)
        barChart.renderer = RoundedBarChartRenderer(barChart, barChart.animator, barChart.viewPortHandler)
        setListeners(barChart)

        val data = intent.extras?.getString("raja")
        findViewById<TextView>(R.id.selectedValueTextView).text = data

    }

    private fun setListeners(barChart : BarChart){
        barChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                if (e is BarEntry) {

                    val index = e.x.toInt() // Index of the selected bar
                    val breathValue = breathValues[index]
                    val relaxValue = relaxValues[index]
                    val focusValue = focusValues[index]

//                    // Update a TextView with the selected bar's data
                    val selectedValueTextView = findViewById<TextView>(R.id.selectedValueTextView)
                    selectedValueTextView.text = "Selected Values: Index - $index  Breath - $breathValue, Relax - $relaxValue, Focus - $focusValue"
//                    // Refresh chart to apply changes
//                    // Create and show the popup
//                    val popupView = layoutInflater.inflate(R.layout.bar_chart_popup, null)
//                    val popupText = popupView.findViewById<TextView>(R.id.popupText)
//                    popupText.text = "Index: $index\nBreath: $breathValue\nRelax: $relaxValue\nFocus: $focusValue"
//
//                    val width = LinearLayout.LayoutParams.WRAP_CONTENT
//                    val height = LinearLayout.LayoutParams.WRAP_CONTENT
//                    val focusable = true // If false, the popup will dismiss when touched outside
//                    val popupWindow = PopupWindow(popupView, width, height, focusable)
//
//                    // Show the popup at the right side of the selected bar
//                    val xPos = e.x + 0.5f // Adjusted x position to show the popup to the right of the bar
//                    val trans = barChart.getTransformer(barChart.axisLeft.axisDependency)
//                    val pos = trans.getPixelForValues(xPos, e.y)
//                    popupWindow.showAtLocation(barChart, Gravity.NO_GRAVITY, pos.x + 16, pos.y)

                }
            }

            override fun onNothingSelected() {
                // Handle when nothing is selected, if needed
            }
        })
    }

    private fun createBarChart(barChart: BarChart) {

        // Set x-axis labels (Days of the week)
        val xLabels = listOf("S", "M", "T", "W", "T", "F", "S")

        // Create BarEntry lists for each dataset
        val breathEntries = ArrayList<BarEntry>()
        val relaxEntries = ArrayList<BarEntry>()
        val focusEntries = ArrayList<BarEntry>()

        for (i in xLabels.indices) {
            breathEntries.add(BarEntry(i.toFloat(), breathValues[i]))
            relaxEntries.add(BarEntry(i.toFloat(), relaxValues[i]))
            focusEntries.add(BarEntry(i.toFloat(), focusValues[i]))
        }

        // Create BarDataSets with labels and colors
        val breathDataSet = BarDataSet(breathEntries, "Breath")
        breathDataSet.color = ContextCompat.getColor(this@BarChartActivity, R.color.color_focus)
        breathDataSet.highLightColor = ContextCompat.getColor(this@BarChartActivity, R.color.bar_select_color)

        val relaxDataSet = BarDataSet(relaxEntries, "Relax")
        relaxDataSet.color = ContextCompat.getColor(this@BarChartActivity, R.color.color_relax)
        relaxDataSet.highLightColor = ContextCompat.getColor(this@BarChartActivity, R.color.bar_select_color)

        val focusDataSet = BarDataSet(focusEntries, "Focus")
        focusDataSet.color = ContextCompat.getColor(this@BarChartActivity, R.color.color_breath)
        focusDataSet.highLightColor = ContextCompat.getColor(this@BarChartActivity, R.color.bar_select_color)


        // Combine datasets into a BarData object
        val barData = BarData(breathDataSet, relaxDataSet, focusDataSet)
        barChart.data = barData

        // disable draw bar values on bar
        barData.setDrawValues(false)

        // xAxis customization
        val xAxis: XAxis = barChart.xAxis
        xAxis.textColor = ContextCompat.getColor(this@BarChartActivity, R.color.text_color)
        xAxis.axisLineColor = ContextCompat.getColor(this@BarChartActivity, R.color.text_color) // Set the color of the x-axis line

         // yAxis customization
        val yAxis: YAxis = barChart.axisLeft
        yAxis.axisMinimum = -15f // Set minimum value on y-axis
        yAxis.textColor = ContextCompat.getColor(this@BarChartActivity, R.color.text_color)
        yAxis.gridColor = ContextCompat.getColor(this@BarChartActivity, R.color.text_color) // Set color for the grid lines

        yAxis.enableGridDashedLine(10f, 15f, 0f)
        yAxis.setDrawAxisLine(false)
        yAxis.setDrawGridLines(true)


        // Stack bars together (optional)
        barData.barWidth = 0.2f // Adjust bar width to avoid overlap
//        barChart.setStackedBars(true, 0.2f) // Stack with 0.2f space between bars

        // Customize chart appearance
//        barChart.axisLeft.axisMinimum = -15f // Set minimum value on y-axis
        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(xLabels) // Set x-axis labels
        barChart.description = null // Remove chart description

        // Remove grid lines
        barChart.xAxis.setDrawGridLines(false)
//        barChart.axisLeft.setDrawGridLines(false)

        // Hide left y-axis labels
        barChart.axisRight.isEnabled = false

        // X-axis labels at bottom
        barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM

        // Disable legend
        barChart.legend.isEnabled = false

        // disable zooming
        barChart.setScaleEnabled(false)

        // Animate the chart
        barChart.animateY(1000) // Animate bar entry appearance
    }


}