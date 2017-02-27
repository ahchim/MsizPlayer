package com.ahchim.android.simplemusicplayer;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 * 권한처리를 담당하는 클래스
 *
 * 권한변경시 PERMISSION_ARRAY의 값만 변경해주면 된다.
 * Created by Ahchim on 2017-02-10.
 */
public class PermissionControl {
    // 권한처리 수정
    // 요청할 권한 목록
    public static final String PERMISSION_ARRAY[] = {Manifest.permission.READ_EXTERNAL_STORAGE};

    // 1. 권한체크
    @TargetApi(Build.VERSION_CODES.M)
    public static boolean checkPermission(Activity activity, int req_permission){
        boolean permCheck = true;
        // 1.1 런타임 권한체크
        // 위에 설정한 권한을 반복문을 돌면서 처리한다...
        for(String perm : PERMISSION_ARRAY){
            if(activity.checkSelfPermission(perm) != PackageManager.PERMISSION_GRANTED){
                permCheck = false;
                break;
            }
        }
        // 1.2 퍼미션이 모두 true이면 그냥 프로그램 실행
        if(permCheck){
            return true;
        } else{
            // 1.3 시스템에 권한 요청
            activity.requestPermissions(PERMISSION_ARRAY, req_permission);
            return false;
        }
    }

    // 2. 권한체크 후 콜백 < 사용자가 확인후 시스템이 호출하는 함수
    public static boolean onCheckResult(int[] grantResult){
        //int resultSize = PERMISSION_ARRAY.length;
        boolean checkResult = true;

        // 권한처리 결과값을 반복문을 돌면서 확인한 후 하나라도 승인되지 않았다면 false를 리턴해준다
        for(int result : grantResult){
            // 2.1 배열에 넘긴 런타임권한을 체크해서 승인이 되지 않으면
            if(result != PackageManager.PERMISSION_GRANTED){
                checkResult = false;
                break;
            }
        }

        return checkResult;
    }
}
