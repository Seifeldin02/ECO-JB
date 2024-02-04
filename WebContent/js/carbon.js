var carbonData = JSON.parse(jsonCarbonData);

var electricData = carbonData.slice().sort(function(a, b) {
    return new Date(a.electricDate) - new Date(b.electricDate);
});

var waterData = carbonData.slice().sort(function(a, b) {
    return new Date(a.waterDate) - new Date(b.waterDate);
});

var recycleData = carbonData.slice().sort(function(a, b) {
    return new Date(a.recycleDate) - new Date(b.recycleDate);
});

var electricChart = new Chart(document.getElementById('electricChart'), {
    type: 'line',
    data: {
        labels: electricData.map(function(item) { return item.electricDate; }),
        datasets: [{
            label: 'Electric Usage',
            data: electricData.map(function(item) { return item.electricUsage; }),
            borderColor: 'rgb(75, 192, 192)',
            tension: 0.1
        },
        {
            label: 'Electric Bill',
            data: electricData.map(function(item) { return item.electricBill; }),
            borderColor: 'rgb(255, 99, 132)',
            tension: 0.1
        },
        {
            label: 'Electric Carbon Waste',
            data: electricData.map(function(item) { return item.electricUsage * 0.584; }),
            borderColor: 'rgb(255, 205, 86)',
            tension: 0.1
        }]
    }
});

var waterChart = new Chart(document.getElementById('waterChart'), {
    type: 'line',
    data: {
        labels: waterData.map(function(item) { return item.waterDate; }),
        datasets: [{
            label: 'Water Usage',
            data: waterData.map(function(item) { return item.waterUsage; }),
            borderColor: 'rgb(75, 192, 192)',
            tension: 0.1
        },
        {
            label: 'Water Bill',
            data: waterData.map(function(item) { return item.waterBill; }),
            borderColor: 'rgb(255, 99, 132)',
            tension: 0.1
        },
        {
            label: 'Water Carbon Waste',
            data: waterData.map(function(item) { return item.waterUsage * 0.584; }),
            borderColor: 'rgb(255, 205, 86)',
            tension: 0.1
        }]
    }
});

var recycleChart = new Chart(document.getElementById('recycleChart'), {
    type: 'line',
    data: {
        labels: recycleData.map(function(item) { return item.recycleDate; }),
        datasets: [{
            label: 'Recycle Usage',
            data: recycleData.map(function(item) { return item.recycleUsage; }),
            borderColor: 'rgb(75, 192, 192)',
            tension: 0.1
        },
        {
            label: 'Recycle Bill',
            data: recycleData.map(function(item) { return item.recycleBill; }),
            borderColor: 'rgb(255, 99, 132)',
            tension: 0.1
        },
        {
            label: 'Recycle Carbon Waste',
            data: recycleData.map(function(item) { return item.recycleUsage * 0.584; }),
            borderColor: 'rgb(255, 205, 86)',
            tension: 0.1
        }]
    }
});