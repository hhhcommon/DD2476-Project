10
https://raw.githubusercontent.com/NearbyShops/Nearby-Shops-Android-app/master/app/src/main/java/org/nearbyshops/enduserappnew/Lists/OrderHistoryPaging/ViewModel/OrdersDataFactory.java
package org.nearbyshops.enduserappnew.Lists.OrderHistoryPaging.ViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

public class OrdersDataFactory extends DataSource.Factory {


    private MutableLiveData<OrdersDataSource> sourceLiveData =
            new MutableLiveData<>();

    private OrdersDataSource latestSource;




    @NonNull
    @Override
    public DataSource create() {

        latestSource = new OrdersDataSource();
        sourceLiveData.postValue(latestSource);
        return latestSource;
    }



    public MutableLiveData<OrdersDataSource> getSourceLiveData() {
        return sourceLiveData;
    }



}
