package com.example.myapplication

import androidx.compose.runtime.mutableStateListOf

// Usamos un objeto singleton para que la lista de enfermeros sea la misma en toda la app.
object NurseData {

    // La lista inicial de enfermeros.
    private val initialNurses = listOf(
        Nurse(1, "Pol", "Carvajal", "pol123", "pol.carvajal", "pol@hospital.com"),
        Nurse(2, "Ana", "García", "ana_g", "ana.garcia", "ana.garcia@hospital.com"),
        Nurse(3, "Biel", "Laguna", "biel_l", "biel.laguna", "biel@hospital.com"),
        Nurse(4, "Laura", "Torres", "laura.t", "laura.torres", "laura@hospital.com"),
        Nurse(5, "Carlos", "Ruiz", "charlie", "carlos.ruiz", "carlos@hospital.com")
    )

    // Usamos mutableStateListOf para que Compose pueda reaccionar a los cambios en la lista.
    val nurses = mutableStateListOf<Nurse>().apply {
        addAll(initialNurses)
    }

    // Función para añadir un nuevo enfermero.
    fun addNurse(nurse: Nurse) {
        nurses.add(nurse)
    }
}
