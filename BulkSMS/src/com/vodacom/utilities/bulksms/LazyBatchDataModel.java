package com.vodacom.utilities.bulksms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import com.vodacom.utilities.bulksms.jpa.*;
 
public class LazyBatchDataModel extends LazyDataModel<SmsBatch> {
     
	private static final long serialVersionUID = 1L;
	
	private List<SmsBatch> datasource;

	public LazyBatchDataModel(List<SmsBatch> datasource) {
        this.datasource = datasource;
    }

	@Override
    public void setRowIndex(final int rowIndex) {
        if (rowIndex == -1 || getPageSize() == 0) {
            super.setRowIndex(-1);
        } else {
            super.setRowIndex(rowIndex % getPageSize());
        }
    }
	
    @Override
    public SmsBatch getRowData(String rowKey) {
        for(SmsBatch batches : datasource) {
            if(String.valueOf(batches).equals(rowKey))
                return batches;
        }
 
        return null;
    }
 
    @Override
    public Object getRowKey(SmsBatch batches) {
        return batches.getBatchId();
    }
 
    @Override
    public List<SmsBatch> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) {
        List<SmsBatch> data = new ArrayList<SmsBatch>();
 
        //filter
        for(SmsBatch batches : datasource) {
            boolean match = true;
 
            if (filters != null) {
                for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
                    try {
                        String filterProperty = it.next();
                        Object filterValue = filters.get(filterProperty);
                        String fieldValue = String.valueOf(batches.getClass().getField(filterProperty).get(batches));
 
                        if(filterValue == null || fieldValue.startsWith(filterValue.toString())) {
                            match = true;
                    }
                    else {
                            match = false;
                            break;
                        }
                    } catch(Exception e) {
                        match = false;
                    }
                }
            }
 
            if(match) {
                data.add(batches);
            }
        }
 
        //sort
        if(sortField != null) {
            Collections.sort(data, new LazySorter(sortField, sortOrder));
        }
 
        //rowCount
        int dataSize = data.size();
        this.setRowCount(dataSize);
 
        //paginate
        if(dataSize > pageSize) {
            try {
                return data.subList(first, first + pageSize);
            }
            catch(IndexOutOfBoundsException e) {
                return data.subList(first, first + (dataSize % pageSize));
            }
        }
        else {
            return data;
        }
    }
}