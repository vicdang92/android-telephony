/**
 * Fetches detailed cell information from the Android telephony system.
 * @returns {string} JSON string containing cell info details.
 *
 * Note: Certain actions, such as `"getAllCellInfo"`, require the
 * `ACCESS_FINE_LOCATION` permission to be granted. If permission is not granted,
 */
declare function fetchCellInfo(): string;
/**
 * Executes a specific telephony-related action.
 * @param {string} action - The action to perform. Supported actions:
 *  - "getCarrierInfo": Retrieves carrier details.
 *  - "getAllCellInfo": Fetches all available cell information.
 * @returns {string} JSON string with the result of the action.
 *
 * Note: Certain actions, such as `"getAllCellInfo"`, require the
 * `ACCESS_FINE_LOCATION` permission to be granted. If permission is not granted,
 */
declare function runAction(action: "getCarrierInfo" | "getAllCellInfo"): string;
export { fetchCellInfo, runAction };
//# sourceMappingURL=index.d.ts.map