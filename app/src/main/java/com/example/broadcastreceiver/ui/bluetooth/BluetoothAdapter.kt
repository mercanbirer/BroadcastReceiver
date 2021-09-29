package com.example.broadcastreceiver.ui.bluetooth

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.broadcastreceiver.R
import com.example.broadcastreceiver.databinding.ItemBluetoothBinding

class BluetoothAdapter(
    items: ArrayList<BluetoothDevice>,
    private val ctx: Context
) : ArrayAdapter<BluetoothDevice>(ctx, R.layout.item_bluetooth, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: ItemBluetoothBinding

        if (convertView == null) {
            binding = ItemBluetoothBinding.inflate(LayoutInflater.from(ctx), parent, false)
            binding.root.tag = binding
        } else {
            binding = convertView.tag as ItemBluetoothBinding
        }
        binding.bluetooth = getItem(position)

        return binding.root
    }
}