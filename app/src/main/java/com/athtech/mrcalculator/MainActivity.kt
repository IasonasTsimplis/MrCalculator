package com.athtech.mrcalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {

    private lateinit var userInputTextView: TextView
    private lateinit var resultTextView: TextView

    private var firstNumber: MutableList<Any> = ArrayList()
    private var secondNumber: MutableList<Any> = ArrayList()
    private var lastOperator: String = ""
    private var hasResult: Boolean = false


    // OnCreate state
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    // On PostCreate state
    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        userInputTextView = findViewById<TextView>(R.id.userInputTextView)
        resultTextView = findViewById<TextView>(R.id.resultsTextView)
    }


    // Handle click on any number or decimal/sign button
    //---------------------------------------------------
    fun handleNumberButtonClick(view: View) {
        if (view is Button) {
            if (lastOperator == "") {
                firstNumber.add(view.text)
            }
            else {
                if (hasResult) {
                    userInputTextView.text = ""
                    firstNumber.clear()
                    secondNumber.clear()
                    firstNumber.add(view.text)
                    resultTextView.text = ""
                    lastOperator = ""
                }
                else {
                    secondNumber.add(view.text)
                }
            }
            userInputTextView.append(view.text)
        }
    }


    // Handle click on 'C (Clear)' and " ⌫ (Backspace)"
    //---------------------------------------------------
    fun handleActionButtonClick(view: View,) {
        if (view.id == R.id.btn_clear) {
            userInputTextView.text = ""
            resultTextView.text = ""
            lastOperator = ""
            hasResult = false
            firstNumber.clear()
            secondNumber.clear()
        }
        else if (view.id == R.id.btn_backspace) {
            val length = userInputTextView.length()

            if (length > 0) {

                if (userInputTextView.text.endsWith(lastOperator)) {
                    lastOperator = ""
                }
                else if (userInputTextView.text.endsWith("=")) {
                    resultTextView.text = ""
                    hasResult = false
                }
                else {
                    if (lastOperator == "") {
                        firstNumber.removeLast()
                    }
                    else {
                        secondNumber.removeLast()
                    }
                }

                userInputTextView.text = userInputTextView.text.subSequence(0, length - 1)
            }
        }
    }


    // Handle the click on an 'operator' button
    fun handleOperatorButtonClick(view: View) {

        // If either "+", "-", "x", "/" was clicked
        if (view.id == R.id.btn_addition || view.id == R.id.btn_subtraction ||
            view.id == R.id.btn_multiplication || view.id == R.id.btn_division ) {

            if (lastOperator != "" && !hasResult) {
                Snackbar.make(view, "Cannot add multiple operators", 2500).show()
                return
            }

            if (userInputTextView.length() == 0) {
                Snackbar.make(view, "Cannot add operator with no number", 2500).show()
                return
            }

            if (hasResult) {
                firstNumber.clear()
                secondNumber.clear()
                firstNumber.add(resultTextView.text)
                userInputTextView.text = resultTextView.text
                resultTextView.text = ""
            }

            if (view is Button){
                lastOperator = view.text.toString()
                userInputTextView.append(lastOperator)
                hasResult = false
            }
        }

        // If "=" was clicked
        if (view.id == R.id.btn_equals) {

            if (lastOperator == "" || hasResult) {
                return
            }

            if (firstNumber.size == 0 || secondNumber.size == 0) {
                Snackbar.make(view, "Two numbers and an operator are needed", 2500).show()
                return
            }

            val firstNumberFloat = firstNumber.joinToString(prefix = "", postfix = "", separator = "").toFloat()
            val secondNumberFloat = secondNumber.joinToString(prefix = "", postfix = "", separator = "").toFloat()
            val result = calculate(firstNumberFloat, secondNumberFloat, lastOperator)

            if (result.endsWith(".0")) {
                resultTextView.text = result.replace(".0", "")
            }
            else {
                resultTextView.text = result
            }

            if (view is Button){
                hasResult = true
                userInputTextView.append("=")
            }
        }
    }


    // Perform calculations between two numbers
    private fun calculate(num1 :Float, num2: Float, operator: String): String {
        var res: Float

        if (operator == "+")
            res = num1 + num2
        else if (operator == "-")
            res = num1 - num2
        else if (operator == "x")
            res = num1 * num2
        else
            res = num1 / num2

        return res.toString()
    }
}