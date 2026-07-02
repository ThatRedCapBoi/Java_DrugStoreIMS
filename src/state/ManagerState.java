package state;

import view.DashboardView;
import view.ProductListView;
import view.CategoryListView;
import view.DataExchangeView;
import view.VendorListView;

/**
 *
 * @author Itadori
 */
public class ManagerState implements AppState {

    @Override
    public void apply(Object view) {
        if (view instanceof DashboardView) {
            DashboardView dv = (DashboardView) view;
            dv.setProductFormEnabled(true);
            dv.setDataExchangeEnabled(true);
            dv.setDashboardGenerateEnabled(true);
            dv.setReportEnabled(true);
            dv.setVendorModuleEnabled(true);
        } else if (view instanceof ProductListView) {
            ProductListView pv = (ProductListView) view;
            pv.setDeleteEnabled(true);
        } else if (view instanceof CategoryListView) {
            CategoryListView cv = (CategoryListView) view;
            cv.setAddEnabled(true);
            cv.setEditEnabled(true);
            cv.setDeleteEnabled(true);
        } else if (view instanceof DataExchangeView) {
            DataExchangeView dev = (DataExchangeView) view;
            dev.setImportEnabled(true);
            dev.setExportEnabled(true);
        } else if (view instanceof VendorListView) {
            VendorListView vlv = (VendorListView) view;
            vlv.setAddEnabled(true);
            vlv.setEditEnabled(true);
            vlv.setDeleteEnabled(true);
        }
    }
}
