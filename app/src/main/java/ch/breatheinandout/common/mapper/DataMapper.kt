package ch.breatheinandout.common.mapper

interface DataMapper <DataModel, DomainModel> {
    fun mapToDomainModel(data: DataModel) : DomainModel
    fun mapFromDomainModel(domain: DomainModel) : DataModel
}