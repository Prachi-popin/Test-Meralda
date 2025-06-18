#!/bin/bash

# Clear previous flag files
rm -f /tmp/call_started.flag /tmp/seller_joined.flag

echo "Running seller-side test (Script 2) first and waiting for customer to initiate the call..."
mvn test -Dtest=TestReceivingCallsFromPopinSeller &

# Give Script 2 some time to launch and begin waiting
sleep 30

echo "Running customer-side test (Script 1) to initiate the call..."
mvn test -Dtest=TestMeraldaCalls

# Wait for seller-side confirmation
if [ -f /tmp/seller_joined.flag ]; then
  echo "✅ Seller side answered the call successfully!"
else
  echo "❌ Seller did not answer the call (seller_joined.flag not found)."
fi

echo "🎬 Video call flow test completed."


#shellcheck disable=SC1128
