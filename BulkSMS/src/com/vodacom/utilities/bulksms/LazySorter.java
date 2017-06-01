package com.vodacom.utilities.bulksms;

import java.util.Comparator;
import org.primefaces.model.SortOrder;
import com.vodacom.utilities.bulksms.jpa.*;
 
public class LazySorter implements Comparator<SmsBatch> {
 
    private String sortField;
     
    private SortOrder sortOrder;
     
    public LazySorter(String sortField, SortOrder sortOrder) {
        this.sortField = sortField;
        this.sortOrder = sortOrder;
    }
 
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public int compare(SmsBatch batch1, SmsBatch batch2) {
        try {
        	 Object value1 = SmsBatch.class.getField(this.sortField).get(batch1);
             Object value2 = SmsBatch.class.getField(this.sortField).get(batch2);
             int value = ((Comparable)value1).compareTo(value2);
             return SortOrder.DESCENDING.equals(sortOrder) ? value : -1 * value;
        }
        catch(Exception e) {
            throw new RuntimeException();
        }
    }
}
