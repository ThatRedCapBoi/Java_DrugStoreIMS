package state;

import view.DashboardView;
import view.ProductListView;
import view.CategoryListView;
import view.DataExchangeView;
import view.VendorListView;

public class StaffState implements AppState {

    @Override
    public void apply(Object view) {
        if (view instanceof DashboardView) {
            DashboardView dv = (DashboardView) view;
            dv.setProductFormEnabled(true);
            dv.setDataExchangeEnabled(false);
            dv.setDashboardGenerateEnabled(false);
            dv.setReportEnabled(true);
            dv.setVendorModuleEnabled(false);
        } else if (view instanceof ProductListView) {
            ProductListView pv = (ProductListView) view;
            pv.setAddEnabled(true);
            pv.setEditEnabled(true);
            pv.setDeleteEnabled(false);
        } else if (view instanceof CategoryListView) {
            CategoryListView cv = (CategoryListView) view;
            cv.setAddEnabled(false);
            cv.setEditEnabled(false);
            cv.setDeleteEnabled(false);
        } else if (view instanceof DataExchangeView) {
            DataExchangeView dev = (DataExchangeView) view;
            dev.setImportEnabled(false);
            dev.setExportEnabled(false);
        } else if (view instanceof VendorListView) {
            VendorListView vlv = (VendorListView) view;
            vlv.setAddEnabled(false);
            vlv.setEditEnabled(false);
            vlv.setDeleteEnabled(false);
        }
    }
}
