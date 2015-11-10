package com.szenamartin.prefelem;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;



import java.util.ArrayList;
import java.util.List;

public class PreferenceElement extends LinearLayout {

    List<String> descriptionList;
    List<String> valueList;
    List<String> serverCodes = null;
    String sharedPreferenceId;
    Integer selectedIndex = 99;
    TextView value;
    SwitchCompat switchCombat;
    boolean switchable=false;
    boolean editable=false;
    boolean noIcon;
    boolean noIconPadding;
    String titleText;
    LinearLayout switchLinearLayout;
    OnPreferenceValueChangeListener mOnPreferenceValueChangeListener = null;
    SharedPreferences mSharedPreferences;

    public PreferenceElement(Context context, AttributeSet attrs) {
        super(context, attrs);

        descriptionList = new ArrayList<>();
        valueList = new ArrayList<>();
        mSharedPreferences = context.getSharedPreferences("szenasimartin_preference_element", Context.MODE_PRIVATE);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Options, 0, 0);
        sharedPreferenceId =a.getString(R.styleable.Options_shared_preference_id);

        titleText = a.getString(R.styleable.Options_titleText);
        String descText = a.getString(R.styleable.Options_descText);
        int iconColor = a.getColor(R.styleable.Options_iconColor, Color.GRAY);
        Drawable iconSrc = a.getDrawable(R.styleable.Options_iconSrc);
        //mods
        int mod = a.getInt(R.styleable.Options_mod, 0);
        if(mod==1) {
            switchable=true;
        }else if(mod==2) {
            editable=true;
        }
        if(switchable){
            sharedPreferenceId=sharedPreferenceId+"swithable";
        }
        if(editable){
            sharedPreferenceId=sharedPreferenceId+"editable";
        }
        noIcon = a.getBoolean(R.styleable.Options_noIcon, false);
        noIconPadding = a.getBoolean(R.styleable.Options_noIconPadding, false);

        setOrientation(LinearLayout.HORIZONTAL);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.preference_element, this, true);

        ImageView iconImageView = (ImageView) getChildAt(0);
        iconImageView.setImageDrawable(iconSrc);
        iconImageView.setBackgroundColor(iconColor);
        LinearLayout linearLayout = (LinearLayout) getChildAt(1);
        TextView titel = (TextView) linearLayout.getChildAt(0);
        titel.setText(titleText);
        titel.setTextColor(iconColor);
        TextView desc = (TextView) linearLayout.getChildAt(1);
        desc.setText(descText);
        value = (TextView) getChildAt(2);
        switchLinearLayout = (LinearLayout) getChildAt(3);
        switchCombat = (SwitchCompat) switchLinearLayout.getChildAt(0);

        if (mSharedPreferences.contains(sharedPreferenceId)) {
            if(editable){
                value.setText(mSharedPreferences.getString(sharedPreferenceId,"no data1"));
            }else if (switchable ) {
                if (mSharedPreferences.getBoolean(sharedPreferenceId, false)) {
                    switchCombat.setChecked(true);
                }
            } else {
                if (!isInEditMode()) {
                    selectedIndex = Integer.valueOf(mSharedPreferences.getString(sharedPreferenceId, "0"));
                }


            }

        }


        initUI();
        initClicks();
        a.recycle();

    }

    private void initUI() {
        if(switchable){
            value.setVisibility(GONE);
            switchLinearLayout.setVisibility(VISIBLE);
        }

    }

    private void initClicks() {
        if(editable){
            this.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    showEditTextDialog();
                }
            });
        }else if (!switchable) {
            this.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    showSelectDialog();
                }
            });
        } else {
            switchCombat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        mSharedPreferences.edit().putBoolean(sharedPreferenceId, true).apply();
                    } else {
                        mSharedPreferences.edit().putBoolean(sharedPreferenceId, false).apply();
                    }
                    if (mOnPreferenceValueChangeListener != null) {
                        if (isChecked) {
                            mOnPreferenceValueChangeListener.onValueChanged("true");
                        } else {
                            mOnPreferenceValueChangeListener.onValueChanged("false");
                        }
                    }

                }
            });
        }

    }


    public void setmOnPreferenceValueChangeListener(OnPreferenceValueChangeListener mOnPreferenceValueChangeListener) {
        this.mOnPreferenceValueChangeListener = mOnPreferenceValueChangeListener;
    }

    private void showSelectDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Select");
        builder.setSingleChoiceItems(descriptionList.toArray(new CharSequence[descriptionList.size()]), selectedIndex, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedIndex = which;
                chagedValue();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void showEditTextDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Value");
        final EditText editText=new EditText(getContext());
        editText.setText(value.getText());//TODO szepiteni
        builder.setView(editText);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                chagedValueByEditText(editText.getText().toString());
            }
        });
        /*builder.setSingleChoiceItems(descriptionList.toArray(new CharSequence[descriptionList
                .size()]), selectedIndex, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedIndex = which;
                chagedValue();
                dialog.dismiss();
            }
        });*/
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void chagedValueByEditText(String newvalue) {
        value.setText(newvalue);
        mSharedPreferences.edit().putString(sharedPreferenceId,newvalue).apply();
        if (mOnPreferenceValueChangeListener != null) {
            mOnPreferenceValueChangeListener.onValueChanged(newvalue);
        }
    }

    public void chagedValue() {
        mSharedPreferences.edit().putString(sharedPreferenceId, selectedIndex.toString()).apply();
        value.setText(valueList.get(selectedIndex));

        if (mOnPreferenceValueChangeListener != null) {
            if (serverCodes == null) {
                mOnPreferenceValueChangeListener.onValueChanged(valueList.get(selectedIndex));
            } else {
                mOnPreferenceValueChangeListener.onValueChanged(serverCodes.get(selectedIndex));
            }

        }

    }


    public PreferenceElement(Context context) {
        this(context, null);
    }


    public void create(ArrayList<String> descriptions, ArrayList<String> values) {
        if (descriptions.size() == values.size()) {
            for (int i = 0; i < descriptions.size(); i++) {
                descriptionList.add(descriptions.get(i));
                valueList.add(values.get(i));
            }
        } else {
            Log.e("createWithLists", "lists not equal");
        }

        if (selectedIndex != -1 && selectedIndex != 99) {
            this.value.setText(valueList.get(selectedIndex));

        }


    }

    public void setSelectedIndexbyServerCode(String value) {
        selectedIndex = serverCodes.indexOf(value);
        if (selectedIndex != -1) {
            this.value.setText(valueList.get(selectedIndex));

        }

    }

    public void setSelectedIndexbyValues(String value) {
        selectedIndex = valueList.indexOf(value);
        if (selectedIndex != -1) {
            this.value.setText(valueList.get(selectedIndex));

        }

    }

    public void setServerCodes(ArrayList<String> serverCodes) {
        this.serverCodes = new ArrayList<>();
        for (String item : serverCodes) {
            this.serverCodes.add(item);
        }
    }

    public String getSelectedCode() {
        if (serverCodes != null) {
            if (selectedIndex < serverCodes.size())
                return serverCodes.get(selectedIndex);
        }

        return null;
    }

    public interface OnPreferenceValueChangeListener {
        void onValueChanged(String newValue);
    }


}
