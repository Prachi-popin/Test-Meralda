#!/bin/bash

# Step 1: Clear previous flag files
rm -f /tmp/call_started.flag /tmp/seller_joined.flag /tmp/ready_for_call.flag
echo "🚀 Starting Appium server..."
nohup appium --log /tmp/appium.log > /dev/null 2>&1 &
sleep 10

echo "📱 Waiting for emulator to boot..."
adb wait-for-device
adb shell 'while [[ -z $(getprop sys.boot_completed) ]]; do sleep 1; done;'
echo "✅ Emulator is fully booted."

echo "🚀 Running seller-side test (Script 2) first..."
mvn test -Dtest=TestReceivingCallsFromPopinSeller &

# Step 2: Wait for ready_for_call.flag created by seller-side script
echo "⏳ Waiting for /tmp/ready_for_call.flag from seller side..."
while [ ! -f /tmp/ready_for_call.flag ]; do
    sleep 2
done

echo "✅ Seller is ready for the call. Starting customer-side test (Script 1)..."

# Step 3: Start customer-side test
mvn test -Dtest=TestMeraldaCalls

# Step 4: Wait and confirm if seller actually joined
if [ -f /tmp/seller_joined.flag ]; then
  echo "✅ Seller side answered the call successfully!"
else
  echo "❌ Seller did not answer the call (seller_joined.flag not found)."
fi

echo "🎬 Video call flow test completed."
