package org.brucknem.distractiontracker.data

import java.text.SimpleDateFormat
import java.util.*

data class Entry(
    var id: Long,
    var datetime: Long,
    var distraction: String,
    var howFeeling: String,
    var internalTrigger: Boolean,
    var externalTrigger: Boolean,
    var planningProblem: Boolean,
    var ideas: String
) {
    constructor(
        datetime: Long,
        distraction: String,
        howFeeling: String,
        internalTrigger: Boolean,
        externalTrigger: Boolean,
        planningProblem: Boolean,
        ideas: String
    ) : this(
        id = Calendar.getInstance().timeInMillis,
        datetime = datetime,
        distraction = distraction,
        howFeeling = howFeeling,
        internalTrigger = internalTrigger,
        externalTrigger = externalTrigger,
        planningProblem = planningProblem,
        ideas = ideas
    )

    constructor() : this(
        datetime = Calendar.getInstance().timeInMillis,
        distraction = "",
        howFeeling = "",
        internalTrigger = true,
        externalTrigger = false,
        planningProblem = false,
        ideas = ""
    )

    constructor(map: Map<String, Any>) : this(
        id = map["id"] as Long,
        datetime = map["datetime"] as Long,
        distraction = map["distraction"] as String,
        howFeeling = map["howFeeling"] as String,
        internalTrigger = map["internalTrigger"] as Boolean,
        externalTrigger = map["externalTrigger"] as Boolean,
        planningProblem = map["planningProblem"] as Boolean,
        ideas = map["ideas"] as String,
    )

    private var dateFormat: java.text.DateFormat = SimpleDateFormat.getDateTimeInstance()

    fun formatDateTime(): String = dateFormat.format(datetime)

    fun isEmpty(): Boolean {
        return distraction.isEmpty() &&
                howFeeling.isEmpty() &&
                ideas.isEmpty()
    }

    fun triggerToString(): String {
        if (internalTrigger) {
            return "Internal Trigger"
        }
        if (externalTrigger) {
            return "External Trigger"
        }
        return "Planning Problem"
    }
}
