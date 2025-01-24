
const handle_delete = (id) => {
    if(confirm("Do you want to delete?")){
        $.ajax({
             url: `/delete-employee/${id}`,
             type: 'GET',
             success: function(response) {
                alert("Employee deleted successfully");
                window.location.href = "/dashboard";
             },
             error: function(error) {
                alert("Error deleting employee:", error);
             }
        });
    }
}