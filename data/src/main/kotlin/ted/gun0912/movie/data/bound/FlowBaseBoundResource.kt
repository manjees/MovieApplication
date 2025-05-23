package ted.gun0912.movie.data.bound

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import ted.gun0912.movie.data.toDomainModel
import ted.gun0912.movie.data_resource.DataResource

abstract class FlowBaseBoundResource<DomainType, DataType>(private val dataAction: suspend () -> DataType) :
    Flow<DataResource<DomainType>> {

    protected open suspend fun fetchFromSource(
        collector: FlowCollector<DataResource<DomainType>>,
        successAction: (suspend (DataType) -> Unit)? = null,
    ) {
        try {
            val data = dataAction()
            val domainModel: DomainType = data.toDomainModel()
            collector.emit(DataResource.success(domainModel))
            successAction?.invoke(data)
        } catch (exception: Exception) {
            collector.emit(DataResource.error(exception))
        }
    }

}
