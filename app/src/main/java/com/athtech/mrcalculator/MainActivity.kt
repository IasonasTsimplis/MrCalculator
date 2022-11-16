package com.athtech.mrcalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {

    private lateinit var userInputTextView: TextView;
    private lateinit var resultTextView: TextView;

    private var firstNumber: MutableList<Any> = ArrayList();
    private var secondNumber: MutableList<Any> = ArrayList();
    private var lastOperator: String = "";


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        userInputTextView = findViewById<TextView>(R.id.userInputTextView);
        resultTextView = findViewById<TextView>(R.id.resultsTextView);
    }

    // Handle click on any number or decimal/sign button
    //---------------------------------------------------
    fun handleNumberButtonClick(view: View) {
        if (view is Button) {
            if (lastOperator == "") {
                firstNumber.add(view.text);
            }
            else {
                if (lastOperator == "=") {
                    userInputTextView.text = "";
                    firstNumber.clear();
                    secondNumber.clear();
                    firstNumber.add(view.text);
                    resultTextView.text = "";
                    lastOperator = "";
                }
                else {
                    secondNumber.add(view.text);
                }
            }
            userInputTextView.append(view.text);
            println(firstNumber);
            println(secondNumber);
        }
    }

    // Handle click on 'C (Clear)' and " ⌫ (Backspace)"
    //---------------------------------------------------
    fun handleActionButtonClick(view: View,) {
        if (view.id == R.id.btn_clear) {
            userInputTextView.text = "";
            resultTextView.text = "";
            lastOperator = "";
            firstNumber.clear();
            secondNumber.clear();
        }
        else if (view.id == R.id.btn_backspace) {
            val length = userInputTextView.length();

            if (length > 0) {

                userInputTextView.text = userInputTextView.text.subSequence(0, length - 1);
            }
        }
    }

    // Handle the click on an operator button
    fun handleOperatorButtonClick(view: View) {

        // On either "+", "-", "x", "/" click
        if (view.id == R.id.btn_addition || view.id == R.id.btn_subtraction ||
            view.id == R.id.btn_multiplication || view.id == R.id.btn_division ) {

            if (lastOperator != "" && lastOperator != "=") {
                Snackbar.make(view, "Cannot add multiple operators", 2500).show();
                return;
            }

            if (userInputTextView.length() == 0) {
                Snackbar.make(view, "Cannot add operator with no number", 2500).show();
                return;
            }

            if (lastOperator == "=") {
                firstNumber.clear();
                secondNumber.clear();
                firstNumber.add(resultTextView.text);
                userInputTextView.text = resultTextView.text;
                resultTextView.text = "";
            }

            if (view is Button){
                lastOperator = view.text.toString();
                userInputTextView.append(lastOperator);
            }
        }

        // On "=" click
        if (view.id == R.id.btn_equals) {

            if (lastOperator == "" || lastOperator == "=") {
                return;
            }

            if (firstNumber.size == 0 || secondNumber.size == 0 || lastOperator == "") {
                Snackbar.make(view, "Two numbers and an operator are needed", 2500).show();
                return;
            }

            val firstNumberFloat = firstNumber.joinToString(prefix = "", postfix = "", separator = "").toFloat();
            val secondNumberFloat = secondNumber.joinToString(prefix = "", postfix = "", separator = "").toFloat();
            val result = calculate(firstNumberFloat, secondNumberFloat, lastOperator);
            resultTextView.text = result.toString();

            if (view is Button){
                lastOperator = view.text.toString();
                userInputTextView.append(lastOperator);
            }
        }
    }

    // Validation when clicking on any operator
    private fun validateOperatorEntry(view: View): Boolean {
        var isValid = true;

        if (view is Button) {

            if (view.text == "+" || view.text == "-" || view.text == "x" || view.text == "/") {
                if (lastOperator != "" && lastOperator != "=") {
                    Snackbar.make(view, "Cannot add multiple operators !", 2500).show();
                    isValid = false;
                }
                if (userInputTextView.length() == 0) {
                    Snackbar.make(view, "Cannot add operator with no number !", 2500).show();
                    isValid = false;
                }
            } else {
                if (lastOperator == "" || lastOperator == "=") {
                    isValid = false;
                }
            }
        }

        return isValid;
    }

    private fun calculate(num1 :Float, num2: Float, operator: String): Float {
        var result = 0.0F;

        if (operator == "+")
            result = num1 + num2;
        else if (operator == "-")
            result = num1 - num2;
        else if (operator == "x")
            result = num1 * num2;
        else
            result = num1 / num2;

        return result;
    }
}