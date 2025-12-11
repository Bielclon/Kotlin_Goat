package com.example.myapplication

interface NurseService {
    fun getAllNurses(): List<Nurse>
}

class MockNurseService : NurseService {
    override fun getAllNurses(): List<Nurse> {
        return listOf(
            Nurse(1, "Pol", "Carvajal", "pol123", "pol.carvajal", "pol@hospital.com"),
            Nurse(2, "Ana", "García", "ana_g", "ana.garcia", "ana.garcia@hospital.com"),
            Nurse(3, "Biel", "Laguna", "biel_l", "biel.laguna", "biel@hospital.com"),
            Nurse(4, "Laura", "Torres", "laura.t", "laura.torres", "laura@hospital.com"),
            Nurse(5, "Carlos", "Ruiz", "charlie", "carlos.ruiz", "carlos@hospital.com")
        )
    }
}
