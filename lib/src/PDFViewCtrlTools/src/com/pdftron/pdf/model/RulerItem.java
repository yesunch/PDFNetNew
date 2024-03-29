package com.pdftron.pdf.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.pdftron.common.PDFNetException;
import com.pdftron.pdf.Annot;
import com.pdftron.sdf.DictIterator;
import com.pdftron.sdf.Obj;

public class RulerItem implements Parcelable {

    public float mRulerBase; // document
    public String mRulerBaseUnit = "";
    public float mRulerTranslate; // world
    public String mRulerTranslateUnit = "";
    public int mPrecision;

    public RulerItem() {
    }

    public RulerItem(float base, String baseUnit, float translate, String translateUnit, int precision) {
        this.mRulerBase = base;
        this.mRulerBaseUnit = baseUnit;
        this.mRulerTranslate = translate;
        this.mRulerTranslateUnit = translateUnit;
        this.mPrecision = precision;
    }

    public RulerItem(RulerItem rulerItem) {
        this.mRulerBase = rulerItem.mRulerBase;
        this.mRulerBaseUnit = rulerItem.mRulerBaseUnit;
        this.mRulerTranslate = rulerItem.mRulerTranslate;
        this.mRulerTranslateUnit = rulerItem.mRulerTranslateUnit;
        this.mPrecision = rulerItem.mPrecision;
    }

    protected RulerItem(Parcel in) {
        mRulerBase = in.readFloat();
        mRulerBaseUnit = in.readString();
        mRulerTranslate = in.readFloat();
        mRulerTranslateUnit = in.readString();
        mPrecision = in.readInt();
    }

    public static final Creator<RulerItem> CREATOR = new Creator<RulerItem>() {
        @Override
        public RulerItem createFromParcel(Parcel in) {
            return new RulerItem(in);
        }

        @Override
        public RulerItem[] newArray(int size) {
            return new RulerItem[size];
        }
    };

    public static void removeRulerItem(Annot annot) {
        try {
            if (annot == null || !annot.isValid()) {
                return;
            }
            Obj obj = annot.getSDFObj();
            if (obj.get(AnnotStyle.KEY_PDFTRON_RULER) != null) {
                obj.erase(AnnotStyle.KEY_PDFTRON_RULER);
            }
        } catch (PDFNetException ignored) {

        }
    }

    @Deprecated
    public static void saveToAnnot(Annot annot, RulerItem rulerItem) throws PDFNetException {
        if (rulerItem == null) {
            return;
        }
        if (annot != null && annot.isValid()) {
            Obj obj = annot.getSDFObj();
            Obj rulerObj = obj.putDict(AnnotStyle.KEY_PDFTRON_RULER);
            rulerObj.putString(AnnotStyle.KEY_RULER_BASE, String.valueOf(rulerItem.mRulerBase));
            rulerObj.putString(AnnotStyle.KEY_RULER_BASE_UNIT, rulerItem.mRulerBaseUnit);
            rulerObj.putString(AnnotStyle.KEY_RULER_TRANSLATE, String.valueOf(rulerItem.mRulerTranslate));
            rulerObj.putString(AnnotStyle.KEY_RULER_TRANSLATE_UNIT, rulerItem.mRulerTranslateUnit);
        }
    }

    @Deprecated
    public static RulerItem getRulerItem(Annot annot) {
        try {
            if (annot == null || !annot.isValid()) {
                return null;
            }
            Obj obj = annot.getSDFObj();
            if (obj.get(AnnotStyle.KEY_PDFTRON_RULER) != null) {
                RulerItem rulerItem = new RulerItem();
                Obj rulerObj = obj.get(AnnotStyle.KEY_PDFTRON_RULER).value();
                DictIterator rulerItr = rulerObj.getDictIterator();
                if (rulerItr != null) {
                    while (rulerItr.hasNext()) {
                        String key = rulerItr.key().getName();
                        String val = rulerItr.value().getAsPDFText();

                        if (key.equals(AnnotStyle.KEY_RULER_BASE)) {
                            rulerItem.mRulerBase = Float.valueOf(val);
                        } else if (key.equals(AnnotStyle.KEY_RULER_BASE_UNIT)) {
                            rulerItem.mRulerBaseUnit = val;
                        } else if (key.equals(AnnotStyle.KEY_RULER_TRANSLATE)) {
                            rulerItem.mRulerTranslate = Float.valueOf(val);
                        } else if (key.equals(AnnotStyle.KEY_RULER_TRANSLATE_UNIT)) {
                            rulerItem.mRulerTranslateUnit = val;
                        }

                        rulerItr.next();
                    }
                    return rulerItem;
                }
            }
        } catch (PDFNetException ignored) {

        }
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(mRulerBase);
        dest.writeString(mRulerBaseUnit);
        dest.writeFloat(mRulerTranslate);
        dest.writeString(mRulerTranslateUnit);
        dest.writeInt(mPrecision);
    }

    @NonNull
    @Override
    public String toString() {
        return "RulerItem:\ndocument scale: " + mRulerBase + " " + mRulerBaseUnit +
            "\nworld scale: " + mRulerTranslate + " " + mRulerTranslateUnit +
            "\nprecision: " + mPrecision;
    }
}
