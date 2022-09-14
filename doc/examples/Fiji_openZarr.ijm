// direct openers (no dialogs)
run("Open Zarr as Virtual Stack", "zarrfolder=/home/ulman/M_TRIF_testSmall/testNew_v4.zarr");
run("Open Zarr in BDV", "zarrfolder=/home/ulman/M_TRIF_testSmall/testNew_v4.zarr");

// opens into a dialog first (with prefilled path to the dataset)
run("Open Zarr Dialog", "zarrfolder=/home/ulman/M_TRIF_testSmall/testNew_v4.zarr");

// when the "dnd dialog action" item shall not appear in the dialog, use e.g. this:
run("Open Zarr Dialog", "zarrfolder=/home/ulman/M_TRIF_testSmall/testNew_v4.zarr  futuredialogaction=dontcare");



