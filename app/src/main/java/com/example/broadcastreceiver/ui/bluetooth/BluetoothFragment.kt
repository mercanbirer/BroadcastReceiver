package com.example.broadcastreceiver.ui.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.*
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.broadcastreceiver.R
import com.example.broadcastreceiver.base.BaseFragment
import com.example.broadcastreceiver.databinding.FragmentBluetoothBinding
import com.github.ajalt.timberkt.e
import org.greenrobot.eventbus.EventBus

class BluetoothFragment : BaseFragment<FragmentBluetoothBinding>(R.layout.fragment_bluetooth) {

    var blAdapter: BluetoothAdapter? = null
    private var deviceItemList = ArrayList<BluetoothDevice>()
    private var mPairingRequestReceiver: BroadcastReceiver? = null
    private var bReciever: BroadcastReceiver? = null
    private val filters = IntentFilter(BluetoothDevice.ACTION_FOUND)
    private var filter1: IntentFilter? = IntentFilter("android.bluetooth.device.action.PAIRING_REQUEST")
    var bTAdapter: android.bluetooth.BluetoothAdapter = android.bluetooth.BluetoothAdapter.getDefaultAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pairedDevices = bTAdapter.bondedDevices
        if (pairedDevices.size > 0) {
            for (device: BluetoothDevice in pairedDevices) {
                deviceItemList.add(device)
            }
        }
    }

    @SuppressLint("WrongConstant", "LogNotTimber")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        e { "GİRDİ" }
        binding.listview.requestFocus()
        blAdapter = BluetoothAdapter(deviceItemList, requireContext())
        binding.listview.adapter = blAdapter
        binding.listview.dividerHeight = 10
        blAdapter!!.notifyDataSetChanged()
        bTAdapter.startDiscovery()
        bTAdapter.enable()


        bReciever = object : BroadcastReceiver() {
            @SuppressLint("LogNotTimber")
            override fun onReceive(context: Context, intent: Intent) {
                val action = intent.action
                if (BluetoothDevice.ACTION_FOUND == action) {

                    Log.e("DEVICELIST", "Bluetooth device found\n")
                    val device =
                        intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                    device?.let {
                        deviceItemList.add(device)
                        Log.e("dekmlf", deviceItemList.toString())
                    }
                    blAdapter!!.add(device)
                    blAdapter!!.notifyDataSetChanged()

                }
            }

        }
        mPairingRequestReceiver = object : BroadcastReceiver() {
            @SuppressLint("LogNotTimber")
            override fun onReceive(context: Context?, intent: Intent) {
                val action = intent.action
                if (action == BluetoothDevice.ACTION_PAIRING_REQUEST) {
                    try {
                        val device: BluetoothDevice =
                            intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)!!
                        val pin =
                            intent.getIntExtra("android.bluetooth.device.extra.PAIRING_KEY", 1234)
                        Log.e(
                            TAG,
                            "Start Auto Pairing. PIN = " + intent.getIntExtra(
                                "android.bluetooth.device.extra.PAIRING_KEY",
                                1234
                            )
                        )
                        val pinBytes: ByteArray = ("" + pin).toByteArray(charset("UTF-8"))
                        device.setPin(pinBytes)
                        device.setPairingConfirmation(true)
                    } catch (e: java.lang.Exception) {
                        Log.e(TAG, "Error occurs when trying to auto pair")
                        e.printStackTrace()
                    }
                }
            }
        }
//        EventBus.getDefault().register(this)

    }
    override fun onStart() {
        super.onStart()
        requireActivity().registerReceiver(bReciever, filters)
        requireActivity().registerReceiver(mPairingRequestReceiver, filter1)
    }

    override fun onStop() {
        super.onStop()
        try {
            requireActivity().unregisterReceiver(bReciever)
            requireActivity().unregisterReceiver(mPairingRequestReceiver)
        } catch (exception: ReceiverCallNotAllowedException) {
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)

    }
}
