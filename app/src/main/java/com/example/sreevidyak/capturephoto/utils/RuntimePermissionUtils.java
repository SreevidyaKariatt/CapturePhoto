package com.example.sreevidyak.capturephoto.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

/**
 * Created by Sreevidya K on 7/24/2017.
 */

public class RuntimePermissionUtils {

    /**
     * unique Keys for  Requested Permissions
     */
    public interface PermissionKeys {
        int REQUEST_WRITE_EXTERNAL_STORAGE = 1234;
        int CAMERA = 1235;
        int READ_SMS = 1236;
        int RECEIVE_SMS = 1237;

    }

    public interface PermissionCallBack {
        void onPermissionGranted();
    }

    /**
     * This method checks whether runtime permissions are granted.
     * <p>Runtime permission for {@code requiredPermission} is requested if permission is not granted.  </p>
     *
     * @param ctx                Activity context
     * @param requiredPermission Should be one among {@link Manifest.permission PermissionKeys}
     * @param permissionKey      unique key based on the permission requested. {@link PermissionKeys}
     * @param fragment           If the permission is requested from Activity, send null if permission is requested from Fragment,
     *                           send fragment instance
     *                           <p>If ActivityCompat.requestPermissions is used for Fragments as wel, then onRequestPermissionsResult() of fragment is never called.
     *                           This param could be avoided if FragmentCompat which is available in v-13 library is used.
     *                           </p>
     * @param callback           {@link PermissionCallBack}
     */
    public static void requestPermission(Context ctx, String requiredPermission, int permissionKey, Fragment fragment, PermissionCallBack callback) {
        if (ContextCompat.checkSelfPermission(ctx, requiredPermission) != PackageManager.PERMISSION_GRANTED) {
            if (fragment != null)
                fragment.requestPermissions(new String[]{requiredPermission}, permissionKey);
            else
                ActivityCompat.requestPermissions((Activity) ctx, new String[]{requiredPermission}, permissionKey);

        } else
            callback.onPermissionGranted();
    }
}
