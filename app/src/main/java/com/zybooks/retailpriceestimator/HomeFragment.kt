package com.zybooks.retailpriceestimator

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.preference.PreferenceManager

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    private lateinit var itemPriceEditText: EditText
    private lateinit var finalPriceTextView: TextView
    private lateinit var saleCheckBox: CheckBox
    private lateinit var clearanceCheckBox: CheckBox
    private lateinit var militaryFirstResponderCheckBox: CheckBox
    private lateinit var taxCheckBox: CheckBox
    private lateinit var calculateBtn: Button
    private lateinit var resetBtn: Button

    private var saleDefaultValue: Double = 0.25
    private var clearanceDefaultValue: Double = 0.3
    private var militaryFirstResponderDefaultValue: Double = 0.2
    private var taxDefaultValue: Double = 0.0775

    private var salePercentValue: Double = saleDefaultValue
    private var clearancePercentValue: Double = clearanceDefaultValue
    private var militaryFirstResponderPercentValue: Double = militaryFirstResponderDefaultValue
    private var taxPercentValue: Double = taxDefaultValue

    // Default of the below discount percentage is 0. meaning if no options selected
    // the item price entered is the final price
    private var saleValueArg: Double = 0.0
    private var clearanceValueArg: Double = 0.0
    private var militaryFirstResponderValueArg: Double = 0.0
    // Default of tax should be 0 because if tax is not selected then there is no
    // tax to be calculated.
    private var taxValueArg: Double = 0.0

    private var NOT_SELECTED_SALE_VALUE: Double = 0.0
    private var TAX_NOT_SELECTED_VALUE: Double = 0.0
    private var ZERO_VALUE: Double = 0.0
    private var MAX_PRICE: Double = 1000.0

    private var initialFinalPrice: String = "0.00"

    private var invalidDialog = InvalidDialogFragment()
    private var invalidOutputDialog = InvalidOutputDialogFragment()
    private var zeroInputDialog = zeroInputDialogFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(requireActivity())

        // Load the Sale percentage from settings
        val newSaleValueStr = sharedPrefs.getString("sale_percentage", "25")
        val newSaleValue = newSaleValueStr?.toDoubleOrNull() ?: (saleDefaultValue * 100.0)
        // make it into a decimal value.
        salePercentValue = newSaleValue / 100.0

        // Load the Clearance Percentage from settings
        val newClearanceValueStr = sharedPrefs.getString("clearance_percentage", "30")
        val newClearanceValue = newClearanceValueStr?.toDoubleOrNull() ?: (clearanceDefaultValue * 100.0)
        // make it into a decimal value.
        clearancePercentValue = newClearanceValue / 100.0

        // Load the Military & First Responders Percentage from settings.
        val newMilitaryFirstResponderValueStr = sharedPrefs.getString("military_first_responders_percentage", "20")
        val newMilitaryFirstResponderValue = newMilitaryFirstResponderValueStr?.toDoubleOrNull() ?: (militaryFirstResponderDefaultValue * 100.0)
        // make it into a decimal value.
        militaryFirstResponderPercentValue = newMilitaryFirstResponderValue / 100.0

        // Load the Tax percentage from settings
        val newTaxValueStr = sharedPrefs.getString("tax_percentage", "7.75")
        val newTaxValue = newTaxValueStr?.toDoubleOrNull() ?: (taxDefaultValue * 100.0)
        // make it into a decimal value
        taxPercentValue = newTaxValue / 100.0



    }

    fun onCheckBoxClicked(view: View) {
        val checked = (view as CheckBox).isChecked
        when (view.id) {
            R.id.checkpoint_sale -> if (checked) {
                // sale checked
                // totalPercentage = totalPercentage * salePercentValue
                saleValueArg = salePercentValue
            } else {
                saleValueArg = NOT_SELECTED_SALE_VALUE
            }
            R.id.checkpoint_clearance -> if (checked) {
                // clearance checked
                // totalPercentage = totalPercentage * clearancePercentValue
                clearanceValueArg = clearancePercentValue
            } else {
                clearanceValueArg = NOT_SELECTED_SALE_VALUE
            }
            R.id.checkpoint_military_first_responder -> if (checked) {
                // military first responder checked
                // totalPercentage = totalPercentage * militaryFirstResponderPercentValue
                militaryFirstResponderValueArg = militaryFirstResponderPercentValue
            } else {
                militaryFirstResponderValueArg = NOT_SELECTED_SALE_VALUE
            }
            R.id.checkpoint_tax -> if (checked) {
                // tax checked
                // totalPercentage = totalPercentage * (1 + taxPercentValue)
                taxValueArg = taxPercentValue
            } else {
                taxValueArg = TAX_NOT_SELECTED_VALUE
            }
        }
    }

    fun calculateClick(view: View) {
        // Get the text that was typed into the EditText
        val itemPriceStr = itemPriceEditText.text.toString()

        // Convert the text into an integer
        val itemPrice = itemPriceStr.toDoubleOrNull() ?: 0.0

        if (itemPrice > MAX_PRICE) {
            invalidDialog.show(parentFragmentManager, "invalidDialog")
            itemPriceEditText.setText("")
            return
        }

        if (itemPrice == ZERO_VALUE) {
            zeroInputDialog.show(parentFragmentManager, "zeroInputDialog")
            itemPriceEditText.setText("")
            return
        }

        val calc = FinalPriceCalculator(itemPrice, saleValueArg, clearanceValueArg,
                                        militaryFirstResponderValueArg, taxValueArg)
        val estimatedFinalPrice = calc.finalPrice

        val finalPriceDouble: Double = estimatedFinalPrice.toDoubleOrNull() ?: 0.0

        if (finalPriceDouble < 0.0) {
            invalidOutputDialog.show(parentFragmentManager, "invalidOutputDialog")
            return
        }

        val estimatedFinalPriceText = getString(R.string.final_price_text, estimatedFinalPrice)
        finalPriceTextView.setText(estimatedFinalPriceText)

    }

    fun resetClick(view: View) {
        // clear item price
        val resetPriceText = ""
        itemPriceEditText.setText(resetPriceText)

        // reset estimated final price
        val initialFinalPriceText = getString(R.string.final_price_text, initialFinalPrice)
        finalPriceTextView.setText(initialFinalPriceText)

        // deselect all checkbox options.
        if (saleCheckBox.isChecked()) {
            saleCheckBox.setChecked(false)
            saleValueArg = NOT_SELECTED_SALE_VALUE
        }
        if (clearanceCheckBox.isChecked()) {
            clearanceCheckBox.setChecked(false)
            clearanceValueArg = NOT_SELECTED_SALE_VALUE
        }
        if (militaryFirstResponderCheckBox.isChecked()) {
            militaryFirstResponderCheckBox.setChecked(false)
            militaryFirstResponderValueArg = NOT_SELECTED_SALE_VALUE
        }
        if (taxCheckBox.isChecked()) {
            taxCheckBox.setChecked(false)
            taxValueArg = NOT_SELECTED_SALE_VALUE
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val parentView = inflater.inflate(R.layout.fragment_home, container, false)

        itemPriceEditText = parentView.findViewById(R.id.item_price_edit_text)
        finalPriceTextView = parentView.findViewById(R.id.final_price_text_view)

        // Set Button and checkbox listeners.
        calculateBtn = parentView.findViewById<Button>(R.id.calc_button)
        calculateBtn.setOnClickListener { calculateClick(calculateBtn) }

        saleCheckBox = parentView.findViewById<CheckBox>(R.id.checkpoint_sale)
        saleCheckBox.setOnClickListener({onCheckBoxClicked(saleCheckBox)})
        registerForContextMenu(saleCheckBox)

        clearanceCheckBox = parentView.findViewById<CheckBox>(R.id.checkpoint_clearance)
        clearanceCheckBox.setOnClickListener({onCheckBoxClicked(clearanceCheckBox)})
        registerForContextMenu(clearanceCheckBox)

        militaryFirstResponderCheckBox = parentView.findViewById<CheckBox>(R.id.checkpoint_military_first_responder)
        militaryFirstResponderCheckBox.setOnClickListener({onCheckBoxClicked(militaryFirstResponderCheckBox)})

        taxCheckBox = parentView.findViewById<CheckBox>(R.id.checkpoint_tax)
        taxCheckBox.setOnClickListener({onCheckBoxClicked(taxCheckBox)})

        resetBtn = parentView.findViewById<Button>(R.id.reset_button)
        resetBtn.setOnClickListener({resetClick(resetBtn)})


        // Set numeric texts
        val initialFinalPriceText = getString(R.string.final_price_text, initialFinalPrice)
        finalPriceTextView.setText(initialFinalPriceText)

        val currentSaleValueText = getString(R.string.sale, (salePercentValue*100.0).toString())
        saleCheckBox.setText(currentSaleValueText)

        val currentClearanceValueText = getString(R.string.clearance, (clearancePercentValue*100.0).toString())
        clearanceCheckBox.setText(currentClearanceValueText)

        val currentMilitaryFirstResponderValueText = getString(R.string.military_first_responders, (militaryFirstResponderPercentValue*100.0).toString())
        militaryFirstResponderCheckBox.setText(currentMilitaryFirstResponderValueText)

        val currentTaxValueText = getString(R.string.tax, (taxPercentValue*100.0).toString())
        taxCheckBox.setText(currentTaxValueText)


        return parentView
    }
}