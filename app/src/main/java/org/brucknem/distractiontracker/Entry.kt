package org.brucknem.distractiontracker

import java.util.*

data class Entry(
    var datetime: Long,
    var distraction: String,
    var howFeeling: String,
    var internal: Boolean,
    var planningProblem: String,
    var ideas: String
) {
    constructor(map: Map<String, Any>) : this(
        datetime = map["datetime"] as Long,
        distraction = map["distraction"] as String,
        howFeeling = map["howFeeling"] as String,
        internal = map["internal"] as Boolean,
        planningProblem = map["planningProblem"] as String,
        ideas = map["ideas"] as String,
    )
}
