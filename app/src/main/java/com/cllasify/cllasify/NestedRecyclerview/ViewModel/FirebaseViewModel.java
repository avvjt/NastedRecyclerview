package com.cllasify.cllasify.NestedRecyclerview.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cllasify.cllasify.NestedRecyclerview.Model.ParentItem;
import com.cllasify.cllasify.NestedRecyclerview.Repo.FirebaseRepo;
import com.google.firebase.database.DatabaseError;

import java.util.List;

public class FirebaseViewModel extends ViewModel implements FirebaseRepo.OnRealtimeDbTaskComplete {

    private MutableLiveData<List<ParentItem>> parentItemMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<DatabaseError> databaseErrorMutableLiveData = new MutableLiveData<>();
    private FirebaseRepo firebaseRepo;

    public MutableLiveData<List<ParentItem>> getParentItemMutableLiveData() {
        return parentItemMutableLiveData;
    }

    public MutableLiveData<DatabaseError> getDatabaseErrorMutableLiveData() {
        return databaseErrorMutableLiveData;
    }

    public FirebaseViewModel(){
        firebaseRepo = new FirebaseRepo(this);
    }

    public void getAllData (){
        firebaseRepo.getAllData();
    }

    @Override
    public void onSuccess(List<ParentItem> parentItemList) {
        parentItemMutableLiveData.setValue(parentItemList);
    }

    @Override
    public void onFailure(DatabaseError error) {
        databaseErrorMutableLiveData.setValue(error);

    }
}
