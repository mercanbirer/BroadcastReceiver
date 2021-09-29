package com.example.broadcastreceiver.ui.bluetooth

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.broadcastreceiver.base.BaseAdapter
import com.example.broadcastreceiver.databinding.ItemBluetoothBinding

class BluetoothAdapter(
    private val context: Context,
    private val data: List<BluetoothDevice>
) : BaseAdapter<BluetoothDevice>(context, data) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding: ItemBluetoothBinding
        if (convertView == null) {
            binding = ItemBluetoothBinding.inflate(LayoutInflater.from(context), parent, false)
            binding.root.tag = binding
        } else {
            binding = convertView.tag as ItemBluetoothBinding
        }
        return binding.root
    }


}