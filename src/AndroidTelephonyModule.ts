import { NativeModule, requireNativeModule } from "expo";
declare class AndroidTelephonyModule extends NativeModule {
  getAllCellInfo(): string;
  execute(action: string): string;
}

// This call loads the native module object from the JSI.
export default requireNativeModule<AndroidTelephonyModule>("AndroidTelephony");
