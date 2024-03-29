/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pdftron.demo.widget.menu;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.internal.view.SupportMenuItem;
import android.support.v4.view.ActionProvider;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

public class ActionMenuItem implements SupportMenuItem {

    private final int mId;
    private final int mGroup;
    private final int mOrdering;

    private CharSequence mTitle;
    private CharSequence mTitleCondensed;
    private Intent mIntent;
    private char mShortcutNumericChar;
    private int mShortcutNumericModifiers = KeyEvent.META_CTRL_ON;
    private char mShortcutAlphabeticChar;
    private int mShortcutAlphabeticModifiers = KeyEvent.META_CTRL_ON;

    private ContextMenuInfo mMenuInfo;

    private Drawable mIconDrawable;
    private int mIconResId = NO_ICON;

    private ActionMenu mMenu;
    private ActionSubMenu mSubMenu;

    private SupportMenuItem.OnMenuItemClickListener mClickListener;

    private CharSequence mContentDescription;
    private CharSequence mTooltipText;

    private ColorStateList mIconTintList = null;
    private PorterDuff.Mode mIconTintMode = null;
    private boolean mHasIconTint = false;
    private boolean mHasIconTintMode = false;

    private static final int NO_ICON = 0;

    private int mFlags = ENABLED;
    private static final int CHECKABLE = 0x00000001;
    private static final int CHECKED = 0x00000002;
    private static final int EXCLUSIVE = 0x00000004;
    private static final int HIDDEN = 0x00000008;
    private static final int ENABLED = 0x00000010;

    public ActionMenuItem(@NonNull ActionMenu menu, int group, int id, int ordering, CharSequence title) {
        mMenu = menu;
        mId = id;
        mGroup = group;
        mOrdering = ordering;
        mTitle = title;
    }

    @Override
    public char getAlphabeticShortcut() {
        return mShortcutAlphabeticChar;
    }

    @Override
    public int getAlphabeticModifiers() {
        return mShortcutAlphabeticModifiers;
    }

    @Override
    public int getGroupId() {
        return mGroup;
    }

    @Override
    public Drawable getIcon() {
        return mIconDrawable;
    }

    public int getIconId() {
        return mIconResId;
    }

    @Override
    public Intent getIntent() {
        return mIntent;
    }

    @Override
    public int getItemId() {
        return mId;
    }

    @Override
    public ContextMenuInfo getMenuInfo() {
        return mMenuInfo;
    }

    @Override
    public char getNumericShortcut() {
        return mShortcutNumericChar;
    }

    @Override
    public int getNumericModifiers() {
        return mShortcutNumericModifiers;
    }

    @Override
    public int getOrder() {
        return mOrdering;
    }

    @Override
    public SubMenu getSubMenu() {
        return mSubMenu;
    }

    @Override
    public CharSequence getTitle() {
        return mTitle;
    }

    @Override
    public CharSequence getTitleCondensed() {
        return mTitleCondensed != null ? mTitleCondensed : mTitle;
    }

    @Override
    public boolean hasSubMenu() {
        return mSubMenu != null;
    }

    public void setSubMenu(ActionSubMenu subMenu) {
        if ((mMenu != null) && (mMenu instanceof SubMenu)) {
            throw new UnsupportedOperationException("Attempt to add a sub-menu to a sub-menu.");
        }

        mSubMenu = subMenu;
        subMenu.setHeaderTitle(getTitle());
    }


    @Override
    public boolean isCheckable() {
        return (mFlags & CHECKABLE) != 0;
    }

    @Override
    public boolean isChecked() {
        return (mFlags & CHECKED) != 0;
    }

    @Override
    public boolean isEnabled() {
        return (mFlags & ENABLED) != 0;
    }

    @Override
    public boolean isVisible() {
        return (mFlags & HIDDEN) == 0;
    }

    @Override
    public MenuItem setAlphabeticShortcut(char alphaChar) {
        mShortcutAlphabeticChar = Character.toLowerCase(alphaChar);
        return this;
    }

    @Override
    public MenuItem setAlphabeticShortcut(char alphaChar, int alphaModifiers) {
        mShortcutAlphabeticChar = Character.toLowerCase(alphaChar);
        mShortcutAlphabeticModifiers = KeyEvent.normalizeMetaState(alphaModifiers);
        return this;
    }

    @Override
    public MenuItem setCheckable(boolean checkable) {
        mFlags = (mFlags & ~CHECKABLE) | (checkable ? CHECKABLE : 0);
        return this;
    }

    public ActionMenuItem setExclusiveCheckable(boolean exclusive) {
        mFlags = (mFlags & ~EXCLUSIVE) | (exclusive ? EXCLUSIVE : 0);
        return this;
    }

    @Override
    public MenuItem setChecked(boolean checked) {
        mFlags = (mFlags & ~CHECKED) | (checked ? CHECKED : 0);
        return this;
    }

    @Override
    public MenuItem setEnabled(boolean enabled) {
        mFlags = (mFlags & ~ENABLED) | (enabled ? ENABLED : 0);
        return this;
    }

    @Override
    public MenuItem setIcon(Drawable icon) {
        mIconDrawable = icon;
        mIconResId = NO_ICON;

        applyIconTint();
        return this;
    }

    @Override
    public MenuItem setIcon(int iconRes) {
        mIconResId = iconRes;
        if (iconRes > 0) {
            mIconDrawable = ContextCompat.getDrawable(mMenu.getContext(), iconRes);
        } else {
            mIconDrawable = null;
        }

        applyIconTint();
        return this;
    }

    @Override
    public MenuItem setIntent(Intent intent) {
        mIntent = intent;
        return this;
    }

    @Override
    public MenuItem setNumericShortcut(char numericChar) {
        mShortcutNumericChar = numericChar;
        return this;
    }

    @Override
    public MenuItem setNumericShortcut(char numericChar, int numericModifiers) {
        mShortcutNumericChar = numericChar;
        mShortcutNumericModifiers = KeyEvent.normalizeMetaState(numericModifiers);
        return this;
    }

    @Override
    public MenuItem setOnMenuItemClickListener(OnMenuItemClickListener menuItemClickListener) {
        mClickListener = menuItemClickListener;
        return this;
    }

    @Override
    public MenuItem setShortcut(char numericChar, char alphaChar) {
        mShortcutNumericChar = numericChar;
        mShortcutAlphabeticChar = Character.toLowerCase(alphaChar);
        return this;
    }

    @Override
    public MenuItem setShortcut(char numericChar, char alphaChar, int numericModifiers,
                                int alphaModifiers) {
        mShortcutNumericChar = numericChar;
        mShortcutNumericModifiers = KeyEvent.normalizeMetaState(numericModifiers);
        mShortcutAlphabeticChar = Character.toLowerCase(alphaChar);
        mShortcutAlphabeticModifiers = KeyEvent.normalizeMetaState(alphaModifiers);
        return this;
    }

    @Override
    public MenuItem setTitle(CharSequence title) {
        mTitle = title;

        if (mSubMenu != null) {
            mSubMenu.setHeaderTitle(title);
        }
        return this;
    }

    public MenuItem setTitle(@StringRes int titleRes) {
        mTitle = mMenu.getContext().getResources().getString(titleRes);
        return this;
    }

    @Override
    public MenuItem setTitleCondensed(CharSequence title) {
        mTitleCondensed = title;
        return this;
    }

    @Override
    public MenuItem setVisible(boolean visible) {
        mFlags = (mFlags & HIDDEN) | (visible ? 0 : HIDDEN);
        return this;
    }

    public boolean invoke() {
        if (mClickListener != null && mClickListener.onMenuItemClick(this)) {
            return true;
        }

        if (mIntent != null) {
            mMenu.getContext().startActivity(mIntent);
            return true;
        }

        return false;
    }

    @Override
    public void setShowAsAction(int show) {
        // Do nothing. ActionMenuItems always show as action buttons.
    }

    @Override
    public SupportMenuItem setActionView(View actionView) {
        throw new UnsupportedOperationException();
    }

    @Override
    public View getActionView() {
        return null;
    }

    @Override
    public MenuItem setActionProvider(android.view.ActionProvider actionProvider) {
        throw new UnsupportedOperationException();
    }

    @Override
    public android.view.ActionProvider getActionProvider() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SupportMenuItem setActionView(int resId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ActionProvider getSupportActionProvider() {
        return null;
    }

    @Override
    public SupportMenuItem setSupportActionProvider(ActionProvider actionProvider) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SupportMenuItem setShowAsActionFlags(int actionEnum) {
        setShowAsAction(actionEnum);
        return this;
    }

    @Override
    public boolean expandActionView() {
        return false;
    }

    @Override
    public boolean collapseActionView() {
        return false;
    }

    @Override
    public boolean isActionViewExpanded() {
        return false;
    }

    @Override
    public MenuItem setOnActionExpandListener(MenuItem.OnActionExpandListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SupportMenuItem setContentDescription(CharSequence contentDescription) {
        mContentDescription = contentDescription;
        return this;
    }

    @Override
    public CharSequence getContentDescription() {
        return mContentDescription;
    }

    @Override
    public SupportMenuItem setTooltipText(CharSequence tooltipText) {
        mTooltipText = tooltipText;
        return this;
    }

    @Override
    public CharSequence getTooltipText() {
        return mTooltipText;
    }

    @Override
    public MenuItem setIconTintList(@Nullable ColorStateList iconTintList) {
        mIconTintList = iconTintList;
        mHasIconTint = true;

        applyIconTint();

        return this;
    }

    @Override
    public ColorStateList getIconTintList() {
        return mIconTintList;
    }

    @Override
    public MenuItem setIconTintMode(PorterDuff.Mode iconTintMode) {
        mIconTintMode = iconTintMode;
        mHasIconTintMode = true;

        applyIconTint();

        return this;
    }

    @Override
    public PorterDuff.Mode getIconTintMode() {
        return mIconTintMode;
    }

    private void applyIconTint() {
        if (mIconDrawable != null && (mHasIconTint || mHasIconTintMode)) {
            mIconDrawable = DrawableCompat.wrap(mIconDrawable);
            mIconDrawable = mIconDrawable.mutate();

            if (mHasIconTint) {
                DrawableCompat.setTintList(mIconDrawable, mIconTintList);
            }

            if (mHasIconTintMode) {
                DrawableCompat.setTintMode(mIconDrawable, mIconTintMode);
            }
        }
    }
}
