package com.samsung.childrensdevelopmentcentermobile.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;

import com.samsung.childrensdevelopmentcentermobile.MainActivity;
import com.samsung.childrensdevelopmentcentermobile.R;
import com.samsung.childrensdevelopmentcentermobile.adapter.DirectionSpinnerAdapter;
import com.samsung.childrensdevelopmentcentermobile.adapter.TutorSpinnerAdapter;
import com.samsung.childrensdevelopmentcentermobile.model.Direction;
import com.samsung.childrensdevelopmentcentermobile.model.Elective;
import com.samsung.childrensdevelopmentcentermobile.model.Tutor;
import com.samsung.childrensdevelopmentcentermobile.nodb.ChildrensDevelopmentCenterSimpleDB;
import com.samsung.childrensdevelopmentcentermobile.rest.impl.ChildrensDevelopmentCenterApiVolleyImpl;

public class ChangeElectiveFragment extends Fragment {

    private EditText etElectiveName;
    private AppCompatSpinner sp_direction;
    private AppCompatSpinner sp_tutor;

    private boolean checkEmpty() {
        boolean problem = false;

        if (TextUtils.isEmpty(etElectiveName.getText().toString())) {
            etElectiveName.setError("Обязательное поле");
            problem = true;
        }

        return problem;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_change_elective, container, false);

        etElectiveName = view.findViewById(R.id.et_electiveName);

        Elective elective = (Elective) (getArguments().getSerializable(MainActivity.MSG_NAME));

        etElectiveName.setText(elective.getName());

        sp_direction = view.findViewById(R.id.sp_direction);
        DirectionSpinnerAdapter directionSpinnerAdapter =
                new DirectionSpinnerAdapter(
                        getActivity(),
                        R.layout.spinner_item,
                        ChildrensDevelopmentCenterSimpleDB.DIRECTION_LIST
                );
        sp_direction.setAdapter(directionSpinnerAdapter);

        sp_tutor = view.findViewById(R.id.sp_tutor);
        TutorSpinnerAdapter tutorSpinnerAdapter =
                new TutorSpinnerAdapter(
                        getActivity(),
                        R.layout.spinner_item,
                        ChildrensDevelopmentCenterSimpleDB.TUTOR_LIST
                );
        sp_tutor.setAdapter(tutorSpinnerAdapter);

        AppCompatButton btn_add = view.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (!checkEmpty()) {


                    new ChildrensDevelopmentCenterApiVolleyImpl(getActivity())
                            .updateElective(
                                    elective.getId(),
                                    etElectiveName.getText().toString(),
                                    ((Direction) sp_direction.getSelectedItem()).getName(),
                                    ((Tutor) sp_tutor.getSelectedItem()).getName()
                            );


                    getActivity().getSupportFragmentManager().beginTransaction().remove(ChangeElectiveFragment.this).commit();

                }
            }
        });

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();

        ((MainActivity) getActivity()).update();
    }


}