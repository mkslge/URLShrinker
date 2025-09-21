// main.js

document.addEventListener("DOMContentLoaded", () => {
    const shortenTab = document.getElementById('shortenTab');
    const analyticsTab = document.getElementById('analyticsTab');
    const shortenForm = document.getElementById('shortenForm');
    const analyticsForm = document.getElementById('analyticsForm');

    // Tab switching
    shortenTab.addEventListener('click', () => {
        shortenTab.classList.add('bg-blue-500', 'text-white');
        shortenTab.classList.remove('bg-gray-200', 'text-gray-700');
        analyticsTab.classList.add('bg-gray-200', 'text-gray-700');
        analyticsTab.classList.remove('bg-green-500', 'text-white');
        shortenForm.classList.remove('hidden');
        analyticsForm.classList.add('hidden');
    });

    analyticsTab.addEventListener('click', () => {
        analyticsTab.classList.add('bg-green-500', 'text-white');
        analyticsTab.classList.remove('bg-gray-200', 'text-gray-700');
        shortenTab.classList.add('bg-gray-200', 'text-gray-700');
        shortenTab.classList.remove('bg-blue-500', 'text-white');
        analyticsForm.classList.remove('hidden');
        shortenForm.classList.add('hidden');
    });

    // Make functions global so they work with onclick
    window.generateShortenedUrl = async function () {
        let originalUrl = document.getElementById("urlInput").value;
        if (!originalUrl.startsWith("https://") && !originalUrl.startsWith("http://")) {
            originalUrl = "https://" + originalUrl;
        }
        let apiUrl = window.location.origin + "/api/urls/create";
        let data = { original_url: originalUrl };

        try {
            const response = await fetch(apiUrl, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data)
            });
            if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);

            const responseData = await response.text();
            const fullUrl = window.location.origin + "/l/" + responseData;

            const shortUrlSpan = document.getElementById('shortUrl');
            const shortenedResultDiv = document.getElementById('shortenedResult');
            const copyButton = document.getElementById('copyButton');

            shortUrlSpan.textContent = fullUrl;
            shortenedResultDiv.classList.remove('hidden');

            // Copy functionality
            copyButton.onclick = () => {
                navigator.clipboard.writeText(fullUrl)
                    .then(() => {
                        copyButton.textContent = "Copied!";
                        setTimeout(() => copyButton.textContent = "Copy", 2000);
                    })
                    .catch(err => console.error("Failed to copy text: ", err));
            };

            console.log(fullUrl);
        } catch (error) {
            console.error("URL couldn't be shortened", error);
        }
    };

    window.getAnalytics = async function () {
        const input = document.getElementById("analyticsInput").value.trim();
        const analyticsResult = document.getElementById("analyticsResult");
        const analyticsMessage = document.getElementById("analyticsMessage");

        if (!input) {
            analyticsMessage.textContent = "Please enter a shortened URL.";
            analyticsMessage.classList.add("text-red-600");
            analyticsResult.classList.remove("hidden");
            return;
        }

        try {
            const shortCode = input.split("/").pop();
            const apiUrl = window.location.origin + "/api/urls/analyze/" + shortCode;

            const response = await fetch(apiUrl);
            const data = await response.text();

            analyticsResult.classList.remove("hidden");

            if (response.status === 404 || parseInt(data) === -1) {
                analyticsMessage.textContent = "Invalid shortened link.";
                analyticsMessage.classList.remove("text-green-600");
                analyticsMessage.classList.add("text-red-600");
            } else {
                analyticsMessage.textContent = `This link has been clicked ${data} times.`;
                analyticsMessage.classList.remove("text-red-600");
                analyticsMessage.classList.add("text-green-600");
            }
        } catch (error) {
            console.error("Error fetching analytics:", error);
            analyticsMessage.textContent = "Something went wrong. Please try again later.";
            analyticsMessage.classList.remove("text-green-600");
            analyticsMessage.classList.add("text-red-600");
            analyticsResult.classList.remove("hidden");
        }
    };
});
