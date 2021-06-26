package org.brucknem.distractiontracker

import android.os.Parcel
import android.os.Parcelable
import java.util.*

data class Entry(
    var datetime: Long,
    var distraction: String?,
    var howFeeling: String?,
    var internal: Boolean,
    var planningProblem: String?,
    var ideas: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    constructor(map: Map<String, Any>) : this(
        datetime = map["datetime"] as Long,
        distraction = map["distraction"] as String,
        howFeeling = map["howFeeling"] as String,
        internal = map["internal"] as Boolean,
        planningProblem = map["planningProblem"] as String,
        ideas = map["ideas"] as String,
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(datetime)
        parcel.writeString(distraction)
        parcel.writeString(howFeeling)
        parcel.writeByte(if (internal) 1 else 0)
        parcel.writeString(planningProblem)
        parcel.writeString(ideas)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Entry> {
        override fun createFromParcel(parcel: Parcel): Entry {
            return Entry(parcel)
        }

        override fun newArray(size: Int): Array<Entry?> {
            return arrayOfNulls(size)
        }
    }
}
