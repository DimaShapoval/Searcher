package com.onpu.domain.mvp.adapter

import com.onpu.domain.data.error.BaseResponseError
import com.onpu.domain.mvp.base.adapter.BaseAdapterMvp
import java.rmi.ServerError

interface SampleAdapterMvp {

    interface Adapter : BaseAdapterMvp.BaseAdapter {
        var items: MutableList<BaseResponseError>
        var stringFreeServers: String
        var stringPremiumServers: String
        var stringLimitedServer: String
        var stringUnlimitedServer: String
        var textOfServerFastest: String
    }

    interface Presenter : BaseAdapterMvp.BasePresenter<Adapter> {
        fun onTitleClick(text: BaseResponseError)

    }

}