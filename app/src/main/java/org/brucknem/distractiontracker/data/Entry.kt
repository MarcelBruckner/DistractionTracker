package org.brucknem.distractiontracker.data

import java.util.*

data class Entry(
    var id: Long,
    var datetime: Long,
    var distraction: String?,
    var howFeeling: String?,
    var internal: Boolean,
    var planningProblem: String?,
    var ideas: String?
) {
    constructor(
        datetime: Long,
        distraction: String?,
        howFeeling: String?,
        internal: Boolean,
        planningProblem: String?,
        ideas: String?
    ) : this(
        id = Calendar.getInstance().timeInMillis,
        datetime = datetime,
        distraction = distraction,
        howFeeling = howFeeling,
        internal = internal,
        planningProblem = planningProblem,
        ideas = ideas
    )

    constructor(map: Map<String, Any>) : this(
        id = map["id"] as Long,
        datetime = map["datetime"] as Long,
        distraction = map["distraction"] as String,
        howFeeling = map["howFeeling"] as String,
        internal = map["internal"] as Boolean,
        planningProblem = map["planningProblem"] as String,
        ideas = map["ideas"] as String,
    )
}
