package org.kodein.db.impl

import org.kodein.db.*
import org.kodein.db.impl.model.Adult
import org.kodein.db.impl.model.Birth
import org.kodein.db.impl.model.City
import org.kodein.db.impl.model.Date
import kotlin.test.Test
import kotlin.test.assertEquals

@Suppress("ClassName")
open class DBTests_00_Find : DBTests() {

    @Test
    fun test00_findAll() {
        db.inflateDB()

        val all = db.findAll().models().toList()
        assertEquals(7, all.size)
        assertEquals(listOf(Models.sjeg, Models.paris, Models.pap), all.subList(0, 3).toList())
        assertEquals(listOf(Models.salomon, Models.laila), all.subList(3, 5))
        assertEquals(listOf("Salomon BRYS: Saint Julien En Genevois", "Laila BRYS-ATIE: Pointe À Pitre"), all.subList(5, 7).map { it as Birth } .map { db[it.adult]!!.fullName + ": " + db[it.city]!!.name } .toList())
    }

    @Test
    fun test01_findAllByType() {
        db.inflateDB()

        assertEquals(listOf(Models.salomon, Models.laila), db.find<Adult>().all().models().toList())
        assertEquals(listOf("Salomon BRYS: Saint Julien En Genevois", "Laila BRYS-ATIE: Pointe À Pitre"), db.find<Birth>().all().models() .map { db[it.adult]!!.fullName + ": " + db[it.city]!!.name } .toList())
        assertEquals(listOf(Models.sjeg, Models.paris, Models.pap), db.find<City>().all().models().toList())
    }

    @Test
    fun test02_findById() {
        db.inflateDB()

        assertEquals(listOf(Models.salomon), db.find<Adult>().byId("BRYS").models().toList())
    }

    @Test
    fun test03_findByIdOpen() {
        db.inflateDB()

        assertEquals(listOf(Models.salomon, Models.laila), db.find<Adult>().byId("BRYS", isOpen = true).models().toList())
    }

    @Test
    fun test04_findAllByIndex() {
        db.inflateDB()

        assertEquals(listOf(Models.paris, Models.pap, Models.sjeg), db.find<City>().byIndex("name").models().toList())
    }

    @Test
    fun test05_findByIndex() {
        db.inflateDB()

        assertEquals(listOf(Models.paris), db.find<City>().byIndex("name", "Paris").models().toList())
    }

    @Test
    fun test06_findByIndexOpen() {
        db.inflateDB()

        assertEquals(listOf(Models.paris, Models.pap), db.find<City>().byIndex("name", "P", isOpen = true).models().toList())
    }

    @Test
    fun test07_getIndexes() {
        db.inflateDB()

        assertEquals(setOf("firstName", "birth"), db.getIndexesOf(db.newKeyFrom(Models.salomon)))
    }

    @Test
    fun test08_entries() {
        db.inflateDB()

        val all = db.findAll().entries().toList()

        assertEquals<Map<Key<Any>, Any>>(hashMapOf(db.newKeyFrom(Models.sjeg) to Models.sjeg, db.newKeyFrom(Models.paris) to Models.paris, db.newKeyFrom(Models.pap) to Models.pap), all.subList(0, 3).associate { it.key to it.model })
        assertEquals<Map<Key<Any>, Any>>(hashMapOf(db.newKeyFrom(Models.laila) to Models.laila, db.newKeyFrom(Models.salomon) to Models.salomon), all.subList(3, 5).associate { it.key to it.model })
        assertEquals(setOf(Birth(db.newKeyFrom(Models.salomon), db.newKeyFrom(Models.sjeg)), Birth(db.newKeyFrom(Models.laila), db.newKeyFrom(Models.pap))), all.subList(5, 7).map { it.model } .toSet())
    }

    @Test
    fun test09_findByCompositeIndex() {
        db.inflateDB()
        val kit = Adult("Kit", "Harington", Date(26, 12, 1986))
        db.put(kit)
        db.put(Adult("Robert", "Pattinson", Date(13, 5, 1986)))

        assertEquals(listOf(Models.salomon, kit), db.find<Adult>().byIndex("birth", 1986, 12).models().toList())
    }

}
