function editProfile() {
    var labels = document.querySelectorAll('.user-details .editable');
    labels.forEach(function(label) {
        var text = label.textContent;
        var inputName = text.split(':')[0].toLowerCase().replace(' ', '');
        var inputValue = text.split(':')[1].trim();
        var inputElement = '';
        switch(inputName) {
            case 'email':
                inputElement = '<input type="email" name="' + inputName + '" value="' + inputValue + '" required />';
                break;
            case 'icno':
                inputElement = '<input type="text" name="' + inputName + '" value="' + inputValue + '" pattern="[A-Za-z0-9]{12}" required />';
                break;
            case 'hpno':
                inputElement = '<input type="tel" name="' + inputName + '" value="' + inputValue + '" pattern="[\+0-9]+" required />';
                break;
            case 'dob':
                inputElement = '<input type="date" name="' + inputName + '" value="' + inputValue + '" required />';
                break;
            case 'hometype':
                inputElement = '<select class="styled-select" name="' + inputName + '">';
                inputElement += '<option value="High-Rise"' + (inputValue === 'High-Rise' ? ' selected' : '') + '>High-Rise</option>';
                inputElement += '<option value="Landed"' + (inputValue === 'Landed' ? ' selected' : '') + '>Landed</option>';
                inputElement += '</select>';
                break;
            default:
                inputElement = '<input type="text" name="' + inputName + '" value="' + inputValue + '" />';
        }
        label.innerHTML = '<strong>' + text.split(':')[0] + ':</strong> ' + inputElement;
    });
    document.getElementById('editButton').style.display = 'none';
    document.getElementById('saveChangesButton').style.display = 'block';
    document.getElementById('Warning').style.display = 'block';
    }

function submitPasswordModal() {
    var password = document.getElementById('password').value;
    console.log("Password: " + password);
    if (password == null || password == "") {
        alert("You must enter your password to save changes.");
        return false;
    }
    document.getElementById('hiddenPassword').value = password; // Set the password to the hiddenPassword input field
    document.getElementById('passwordModal').style.display = 'none';
    document.getElementById('editProfileForm').submit(); // Submit the form
    return true;
}
window.onload = function() {



function showPasswordModal(e) {
    e.preventDefault(); // Prevent the form from being submitted
    document.getElementById('passwordModal').style.display = 'block'; // Show the password modal
}

document.getElementById('saveChangesButton').addEventListener('click', showPasswordModal); 
}
document.addEventListener('DOMContentLoaded', (event) => {
	  // Get the modal and the cancel button
	  var modal = document.getElementById('passwordModal');
	  var cancelBtn = document.getElementsByClassName('cancel-btn')[0];

	  // When the user clicks on the cancel button, close the modal
	  if (cancelBtn) {
	    cancelBtn.onclick = function() {
	      modal.style.display = "none";
	    }
	  }
	});