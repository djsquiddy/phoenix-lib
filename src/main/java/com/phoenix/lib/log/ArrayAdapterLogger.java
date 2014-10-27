/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 phoenix-lib
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.phoenix.lib.log;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.phoenix.lib.R;

import java.util.ArrayList;
import java.util.List;

/**
 * date: 10/26/2014.
 *
 * @author Dylan
 */
public class ArrayAdapterLogger extends BaseLog {
    private final LoggerAdapter mAdapter;

    public ArrayAdapterLogger(LoggerAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    protected void print(@NonNull final LogData logData) {
        if (mAdapter.getContext() instanceof Activity) {
            ((Activity) mAdapter.getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter.add(logData);
                    mAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    public static class LoggerAdapter extends RecyclerView.Adapter<LoggerAdapter.ViewHolder> {
        private final List<LogData> mValues = new ArrayList<LogData>();
        private final Context mContext;

        public LoggerAdapter(final Context context) {
            mContext = context;

        }

        public void add(LogData logData) {
            mValues.add(logData);
        }

        public Context getContext() {
            return mContext;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.viewholder_logger, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int index) {
            viewHolder.mTextView.setText(mValues.get(index).getMsg());
            viewHolder.mTextView.setTextColor(mValues.get(index).priority.getColor(mContext));
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public final TextView mTextView;

            public ViewHolder(View view) {
                super(view);
                mTextView = (TextView) view;
            }
        }
    }

}
