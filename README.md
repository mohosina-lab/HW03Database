# HW03: Database

This task shows basic data input, image selection, view switching using Jetpack Compose.

## Home Screen
The Home screen allows the user to:
- Enter a username using a text input field
- Select an image from the device gallery using the Android Photo Picker
- Preview the selected image
- Navigate to a second screen to view the entered data

## Second Screen
The second screen displays:
- The entered username
- The selected image
- A button to navigate back to the Home screen

## Implementation Details
Text input is implemented using an `OutlinedTextField` in Jetpack Compose, with the entered value stored in a state variable. The username is persisted using **SharedPreferences**, allowing it to be restored when the application restarts.

Image selection is implemented using **ActivityResultContracts.PickVisualMedia** (Android Photo Picker). The selected image is copied into the app’s **internal storage** to ensure long-term access. On application startup, the stored image file is loaded automatically if it exists.

View switching is handled using Compose state to conditionally display either the Home screen or the Second screen. The entered data is shared across views through state variables and restored from persistent storage when needed.

## AI Usage Declaration
Generative AI tools were used to provide guidance and troubleshooting support during the development of this exercise, particularly for resolving emulator crashes and challenges related to image selection and persistence. All implementation decisions and final code were reviewed and written by the author.

## Author
**Mohosina Akhter**
