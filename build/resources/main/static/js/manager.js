
// toggle the filter popover
function toggleFilterPopover(){
  let isVis = $("#filterPopover").css("visibility");
  if(isVis == "visible"){
    showFilterPopover(false);
  }
  else{
    showFilterPopover(true);
  }
}

//show / hide the filter by employee window
function showFilterPopover(bShow){
  if(bShow){
    $("#filterPopover").css("visibility","visible");
  }
  else{
    $("#filterPopover").css("visibility","hidden");
  }
}

//submit the requested filter (from filter popover)
// causes page to reload.
function submitFilter(e){
  let form = $("#employee-filter-form");
  let redirectUrl = "/manager?event-filter=";

  //get values from form
  form.find("span input").each(function (index){
    if(this.checked){
      redirectUrl += this.name + ",";
    }
  });
  $(location).attr('href', redirectUrl.substr(0,redirectUrl.length - 1));
}

// handle keydown events
$(document).bind("keydown", function (e){

  // escape key! close popover (if open)
  if(e.which == 27){
    showFilterPopover(false);
  }
});
