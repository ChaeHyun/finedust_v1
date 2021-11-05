package ch.breatheinandout.database.searchedaddress

import androidx.room.*

@Dao
interface SearchedAddressDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(searchedAddressEntity: SearchedAddressEntity)

    @Query("SELECT * FROM SearchedAddressEntity")
    fun getAll() : List<SearchedAddressEntity>

    @Delete
    fun delete(searchedAddressEntity: SearchedAddressEntity)

    @Query("DELETE FROM LocationAndStationEntity")
    fun dropTable()
}