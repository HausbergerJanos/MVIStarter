package com.hausberger.mvistarter.framework.datasource.network.mapper

import com.hausberger.mvistarter.business.domain.model.Sample
import com.hausberger.mvistarter.business.domain.util.EntityMapper
import com.hausberger.mvistarter.framework.datasource.network.model.SampleListItemNetworkEntity
import javax.inject.Inject

class SampleNetworkMapper
@Inject
constructor() : EntityMapper<SampleListItemNetworkEntity, Sample> {

    override fun mapFromEntity(entityListItem: SampleListItemNetworkEntity): Sample {
        return Sample(
            title = entityListItem.title ?: ""
        )
    }

    override fun mapToEntity(domainModel: Sample): SampleListItemNetworkEntity {
        return SampleListItemNetworkEntity(
            title = domainModel.title
        )
    }

    fun entityListToNoteList(entityListItems: List<SampleListItemNetworkEntity>): List<Sample> {
        return entityListItems.map { sampleNetworkEntity ->
            mapFromEntity(sampleNetworkEntity)
        }
    }
}