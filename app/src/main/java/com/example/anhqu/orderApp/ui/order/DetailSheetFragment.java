package com.example.anhqu.orderApp.ui.order;

import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.anhqu.orderApp.R;
import com.example.anhqu.orderApp.data.database.model.OrderItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by anhqu on 8/6/2018.
 */

public class DetailSheetFragment extends BottomSheetDialogFragment {

    @BindView(R.id.text_name)
    TextView textName;
    @BindView(R.id.btn_minus)
    ImageView btnMinus;
    @BindView(R.id.txt_quant)
    TextView txtQuant;
    @BindView(R.id.btn_plus)
    ImageView btnPlus;
    @BindView(R.id.text_price)
    TextView textPrice;
    @BindView(R.id.btn_update)
    TextView btnAdd;
    Unbinder unbinder;
    private OrderItem item;
    private int orderPos;
    private double currTotalP;
    double excTotalP;
    private EditOrderInterface anInterface;

    public DetailSheetFragment() {
    }

    public void setAnInterface(EditOrderInterface anInterface) {
        this.anInterface = anInterface;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_sheet, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setViewData();
        onViewClicked(view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void setViewData() {
        btnAdd.setText(R.string.action_update_cart);

        // Get data from bundle
        Bundle b = getArguments();
        if (b != null) {
            item = b.getParcelable("order_data");
            orderPos = b.getInt("order_position");

            if (item != null) {
                textName.setText(item.getFood().getfName());
                currTotalP = item.getTotalPrice();
                textPrice.setText(String.format("Price: %s$", item.getTotalPrice()));
                txtQuant.setText(String.format("%s", item.getQuantity()));
            }
        }
    }

    @OnClick({R.id.btn_minus, R.id.btn_plus, R.id.btn_update})
    public void onViewClicked(View view) {
        int quantity = Integer.parseInt(txtQuant.getText().toString());
        boolean isUpdateClicked = false;
        switch (view.getId()) {
            case R.id.btn_minus:
                if (quantity > 0)
                    quantity -= 1;
                break;
            case R.id.btn_plus:
                quantity += 1;
                break;
            case R.id.btn_update:
                item.setQuantity(quantity);
                item.setTotalPrice(quantity * item.getFood().getfPrice());
                if (anInterface != null)
                    anInterface.onEdit(item, orderPos, excTotalP);
                if (this.isVisible())
                    this.dismiss();
                isUpdateClicked = true;
                break;
        }
        if (!isUpdateClicked) {
            int finalQuantity = quantity;
            double newTotalP = quantity * item.getFood().getfPrice();
            excTotalP = newTotalP - currTotalP;

            // Update UI dynamically
            getActivity().runOnUiThread(() -> {
                txtQuant.setText(String.format("%s", finalQuantity));
                textPrice.setText(String.format("Price: %s$", newTotalP));
                btnAdd.setText(R.string.action_update_cart);
                if (finalQuantity == 0) {
                    btnAdd.setText(R.string.action_remove_cart);
                }
            });
        }
    }

    // Listen for change of update
    public interface EditOrderInterface {
        void onEdit(OrderItem item, int position, double excTotalP);
    }
}
