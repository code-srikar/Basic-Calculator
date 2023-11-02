package com.srikar.calculatorapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.srikar.calculatorapp.databinding.ActivityMainBinding
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    var lastNumeric = false
    var lastDot = false
    var stateError = false

    private lateinit var expression: Expression

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun onEqualClick(view: View) {
        onEqual()
        binding.dataTv.text = binding.resultsTv.text.toString().drop(1)
        binding.resultsTv.text = ""
    }

    fun onDigitClick(view: View) {
        if (stateError) {
            binding.dataTv.text = (view as Button).text
            stateError = false
        }
        else {
            if(binding.dataTv.length()<15){
                binding.dataTv.append((view as Button).text)
                lastNumeric = true
                onEqual()
            }
            else{
                Toast.makeText(this,"Maximum limit is 15",Toast.LENGTH_SHORT).show()
            }

        }

    }

    fun onAllClearClick(view: View) {
        binding.dataTv.text = ""
        binding.resultsTv.text = ""
        lastNumeric = false
        lastDot = false
        stateError = false
    }

    fun onOperatorClick(view: View) {
        if (!stateError && lastNumeric) {
            binding.dataTv.append((view as Button).text)
            lastDot = false
            lastNumeric = false
            onEqual()
        }
    }

    fun onClearClick(view: View) {
        binding.dataTv.text = ""
        lastNumeric = false
    }

    fun onBackClick(view: View) {
        binding.dataTv.text = binding.dataTv.text.toString().dropLast(1)

        try {
            val lastChar = binding.dataTv.text.toString().last()
            if (lastChar.isDigit()) {
                onEqual()
            }
        } catch (e: Exception) {
            binding.resultsTv.text = ""
            Log.e("last char error", e.toString())
        }
    }

    fun onEqual() {
        if (lastNumeric && !stateError) {
            val txt = binding.dataTv.text.toString()
            expression = ExpressionBuilder(txt).build()

            try {
                val result = expression.evaluate()
                binding.resultsTv.text = "=" + result.toString()
            } catch (ex: ArithmeticException) {
                Log.e("evaluate error", ex.toString())
                binding.resultsTv.text = "Error"
                stateError = true
                lastNumeric = false
            }
        }
    }
}