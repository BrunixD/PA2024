package `Phase 2`

import main.DirectoryEntity
import main.Entity
import main.addAttribute
import kotlin.reflect.full.declaredMemberProperties


class `prof example` {

    class FUC(
        val codigo: String,
        val nome: String,
        val ects: Double,
        val observacoes: String,

    )

    val f = FUC("M4310", "Programação Avançada", 6.0, "la la...")

    fun translate(obj: Any) {
        val name = obj::class.simpleName
        val newObject = DirectoryEntity(name= name.toString())
        val members = obj::class.declaredMemberProperties
        members.forEach { member ->
            println(member )
            print(member.call().toString())
            newObject.addAttribute(member.toString(), member.call().toString())

        }


    }
    fun main(args: Array<String>) {
       translate(f)
    }
}