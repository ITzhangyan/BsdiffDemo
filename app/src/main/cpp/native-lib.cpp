#include <jni.h>
#include <string>

extern "C" {
extern int main(int argc, char *argv[]);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_example_bsdiffdemo_MainActivity_bsPatch(JNIEnv *env, jobject instance, jstring oldApk_,
                                                 jstring patch_, jstring outputApk_) {
    //把java的字符串转为c/c++的字符串
    const char *oldApk = env->GetStringUTFChars(oldApk_, 0);
    const char *patch = env->GetStringUTFChars(patch_, 0);
    const char *outputApk = env->GetStringUTFChars(outputApk_, 0);

    // 调用了c实现bspatch合成
    //bspatch oldfile newfile patchfile
    const char *argv[] = {"", oldApk, outputApk, patch};
    main(4, const_cast<char **>(argv));

    //gc 释放
    env->ReleaseStringUTFChars(oldApk_, oldApk);
    env->ReleaseStringUTFChars(patch_, patch);
    env->ReleaseStringUTFChars(outputApk_, outputApk);
}
