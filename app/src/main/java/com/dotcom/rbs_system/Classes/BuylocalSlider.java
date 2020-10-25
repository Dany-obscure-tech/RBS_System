package com.dotcom.rbs_system.Classes;

import java.util.ArrayList;
import java.util.List;

public class BuylocalSlider {
    private static BuylocalSlider buylocalSliderobj = new BuylocalSlider();

    List<String> bulocalSlider = new ArrayList<>();

    public List<String> getBuylocalSliderList() {
        return bulocalSlider;
    }

    public void setBuylocalSliderList(List<String> bulocalSlider) {
        this.bulocalSlider = bulocalSlider;
    }

    private BuylocalSlider() {}

    public static BuylocalSlider getInstance() {
        return buylocalSliderobj;
    }
}
