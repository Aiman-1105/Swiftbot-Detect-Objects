# Swiftbot Detect Objects #

- Has 3 interactive modes (Curious Swiftbot, Scaredy Swiftbot and Dubious Swiftbot) which can be selected via QR Code Scan. <br />

### Curious Swiftbot ###

- Wanders at low speeds with its underlights set to blue until it encounters an object.
- When it encounter an object, it switches its underlights to green and adjusts itself to maintain a bufferzone of 15 cm from the object.
- Blinks its underlights green when it reaches the bufferzone of 15 cm and remains stationary.
- Captures an image of the object detected and stores it.
- Waits for 2 seconds and checks if the object has moved or not.
- Adjusts distance to match the bufferzone accordingly.
- If an object is not encountered for 5 seconds, the Swiftbot changes direction and continues wandering at low speed with its underlights set to blue.

### Scaredy Swiftbot ###

- Wanders at low speed with its underlights set to blue until it encounters an object within 1 metre.
- Underlights change to red when an object is encountered.
- Captures an image of the object detected and stores it.
- Blinks its underlights while backing away from the object.
- Turns in the opposite direction and moves away from the object.
- If an object is not encountered for 5 seconds, the Swiftbot changes direction and continues wandering at low speed with its underlights set to blue.

### Dubious Swiftbot ###

- Randomizes and selects one of the 2 modes mentioned above and runs it.

## Execution Logs ##

- After the Swiftbot session is over, it prompts the user to view execution logs.
- If the user selects yes, the execution logs will be displayed to the user.
- The execution logs contain the Mode that Swiftbot had run, the duration of the session, the number of objects the Swiftbot had encountered and the number of images the Swiftbot took.
