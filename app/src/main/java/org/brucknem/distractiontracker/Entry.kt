package org.brucknem.distractiontracker

data class Entry(
    var datetime: Long,
    var distraction: String,
    var howFeeling: String,
    var internal: Boolean,
    var planningProblem: String,
    var ideas: String
)
