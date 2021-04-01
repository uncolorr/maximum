package com.example.maximumhackathon

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

inline fun FragmentManager?.transaction(
    commit: FragmentTransaction.() -> Unit = { commit() },
    action: FragmentTransaction.() -> Unit
) {
    if (this != null) {
        val transaction = beginTransaction()
        action(transaction)
        commit(transaction)
    }
}