import * as telephony from "android-telephony";
import { useState } from "react";
import {
  Button,
  SafeAreaView,
  ScrollView,
  Text,
  StyleSheet,
  View,
} from "react-native";

export default function App() {
  const [value, setValue] = useState<string>(JSON.stringify("{}"));

  const handlePress = async (action: "getCarrierInfo" | "getAllCellInfo") => {
    try {
      const result = await telephony.runAction(action);
      setValue(JSON.stringify(result, null, 2)); // Beautify JSON output
    } catch (error) {
      console.error("Error fetching data:", error);
      setValue("Failed to fetch data.");
    }
  };

  return (
    <SafeAreaView style={styles.container}>
      <ScrollView contentContainerStyle={styles.content}>
        <Text style={styles.header}>Module API Example</Text>

        <View style={styles.action}>
          <Button
            title="Get All Cell Info"
            onPress={() => handlePress("getAllCellInfo")}
          />
          <Button
            title="Get Carrier Info"
            onPress={() => handlePress("getCarrierInfo")}
          />
        </View>

        <Text style={styles.output}>{JSON.parse(value)}</Text>
      </ScrollView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#eee",
  },
  content: {
    padding: 20,
  },
  action: {
    justifyContent: "center",
    flexDirection: "row",
    gap: 16,
  },
  header: {
    fontSize: 24,
    fontWeight: "bold",
    marginBottom: 20,
    alignSelf: "center",
  },
  output: {
    marginTop: 20,
    backgroundColor: "#fff",
    padding: 10,
    borderRadius: 5,
    fontSize: 16,
  },
});
