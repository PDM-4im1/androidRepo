// Generated by view binder compiler. Do not edit!
package com.example.bloggerapp.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.bloggerapp.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class RowBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final ImageView authorAvatar;

  @NonNull
  public final TextView authorName;

  @NonNull
  public final ImageView imageView3;

  @NonNull
  public final LinearLayout postHeader;

  @NonNull
  public final TextView postTime;

  @NonNull
  public final TextView postTitle;

  private RowBinding(@NonNull RelativeLayout rootView, @NonNull ImageView authorAvatar,
      @NonNull TextView authorName, @NonNull ImageView imageView3, @NonNull LinearLayout postHeader,
      @NonNull TextView postTime, @NonNull TextView postTitle) {
    this.rootView = rootView;
    this.authorAvatar = authorAvatar;
    this.authorName = authorName;
    this.imageView3 = imageView3;
    this.postHeader = postHeader;
    this.postTime = postTime;
    this.postTitle = postTitle;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static RowBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static RowBinding inflate(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent,
      boolean attachToParent) {
    View root = inflater.inflate(R.layout.row, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static RowBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.author_avatar;
      ImageView authorAvatar = ViewBindings.findChildViewById(rootView, id);
      if (authorAvatar == null) {
        break missingId;
      }

      id = R.id.author_name;
      TextView authorName = ViewBindings.findChildViewById(rootView, id);
      if (authorName == null) {
        break missingId;
      }

      id = R.id.imageView3;
      ImageView imageView3 = ViewBindings.findChildViewById(rootView, id);
      if (imageView3 == null) {
        break missingId;
      }

      id = R.id.post_header;
      LinearLayout postHeader = ViewBindings.findChildViewById(rootView, id);
      if (postHeader == null) {
        break missingId;
      }

      id = R.id.post_time;
      TextView postTime = ViewBindings.findChildViewById(rootView, id);
      if (postTime == null) {
        break missingId;
      }

      id = R.id.post_title;
      TextView postTitle = ViewBindings.findChildViewById(rootView, id);
      if (postTitle == null) {
        break missingId;
      }

      return new RowBinding((RelativeLayout) rootView, authorAvatar, authorName, imageView3,
          postHeader, postTime, postTitle);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
