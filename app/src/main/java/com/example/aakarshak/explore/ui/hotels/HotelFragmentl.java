package com.example.aakarshak.explore.ui.hotels;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.Group;
import androidx.core.content.ContextCompat;
import androidx.core.widget.TextViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.aakarshak.explore.R;
import com.example.aakarshak.explore.data.local.models.HotelsClass;
import com.example.aakarshak.explore.ui.common.SpacingList;
import com.example.aakarshak.explore.utils.ImageUtility;
import com.example.aakarshak.explore.utils.UtilityColor;
import com.example.aakarshak.explore.utils.UtilityIntent;
import com.example.aakarshak.explore.utils.UtlitySnack;
import com.example.aakarshak.explore.workers.DecoderImaFrag;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

public class HotelFragmentl extends Fragment
        implements ContractHlist.V, SwipeRefreshLayout.OnRefreshListener {

    //Constant used for logs
    private static final String LOG_TAG = HotelFragmentl.class.getSimpleName();

    //Bundle constants for persisting the data throughout System config changes
    private static final String BUNDLE_HOTELS_LIST_KEY = "HotelList.Hotels";

    //The PresenterBase interface for this V
    private ContractHlist.PresenterBase mPresenter;

    //References to the Views shown in this Fragment
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerViewContentList;

    //Adapter of the RecyclerView
    private HotelListAdapter mAdapter;

    public HotelFragmentl() {
    }

    public static HotelFragmentl newInstance() {
        return new HotelFragmentl();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout 'R.layout.layout_main_content_page'
        //Passing false as we are attaching the layout ourselves
        View rootView = inflater.inflate(R.layout.main_content_page, container, false);

        //Finding the Views
        mSwipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh_content_page);
        mRecyclerViewContentList = rootView.findViewById(R.id.recyclerview_content_page);

        //Initialize SwipeRefreshLayout
        setupSwipeRefresh();

        //Initialize RecyclerView
        setupRecyclerView();

        //Setting the background color for the root view
        rootView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.hotelListBackgroundColor));

        //Returning the prepared layout
        return rootView;
    }

    private void setupSwipeRefresh() {
        //Registering the refresh listener which triggers a new load on swipe to refresh
        mSwipeRefreshLayout.setOnRefreshListener(this);
        //Configuring the Colors for Swipe Refresh Progress Indicator
        mSwipeRefreshLayout.setColorSchemeColors(UtilityColor.obtainColorsFromTypedArray(requireContext(), R.array.swipe_refresh_colors, R.color.colorPrimary));
    }

    private void setupRecyclerView() {
        //Creating a Vertical Linear Layout Manager with the default layout order
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL, false);

        //Setting the Layout Manager to use
        mRecyclerViewContentList.setLayoutManager(linearLayoutManager);

        //Initializing the Adapter for the RecyclerView
        mAdapter = new HotelListAdapter(requireContext(), new ItemUserActionHotel());

        //Setting the Adapter for RecyclerView
        mRecyclerViewContentList.setAdapter(mAdapter);

        //Retrieving the Item spacing to use
        int itemSpacing = getResources().getDimensionPixelSize(R.dimen.all_list_items_spacing);

        //Setting Item offsets using Item Decoration
        mRecyclerViewContentList.addItemDecoration(new SpacingList(itemSpacing, itemSpacing));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            //On Subsequent launch

            //Restoring the list of Hotels
            mPresenter.updateHotels(savedInstanceState.getParcelableArrayList(BUNDLE_HOTELS_LIST_KEY));
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        //Saving the State
        outState.putParcelableArrayList(BUNDLE_HOTELS_LIST_KEY, mAdapter.getHotelList());
    }

    @Override
    public void onResume() {
        super.onResume();

        //Start loading the Hotels
        mPresenter.start();
    }

    @Override
    public void onRefresh() {
        //Dispatching the event to the PresenterBase to reload the data
        mPresenter.onRefreshMenuClicked();
    }

    @Override
    public void setPresenter(ContractHlist.PresenterBase presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showProgressIndicator() {
        //Enabling the Swipe to Refresh if disabled prior to showing the Progress indicator
        if (!mSwipeRefreshLayout.isEnabled()) {
            mSwipeRefreshLayout.setEnabled(true);
        }
        //Displaying the Progress Indicator only when not already shown
        if (!mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(true);
        }
    }

    @Override
    public void hideProgressIndicator() {
        //Hiding the Progress indicator
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void loadHotels(List<HotelsClass> hotelsClassList) {
        //Submitting the Hotels data to the Adapter to load
        mAdapter.submitList(hotelsClassList);
    }

    @Override
    public void showError(int messageId, @Nullable Object... args) {
        if (getView() != null) {
            //When we have the root view

            //Evaluating the message to be shown
            String messageToBeShown;
            if (args != null && args.length > 0) {
                //For the String Resource with args
                messageToBeShown = getString(messageId, args);
            } else {
                //For the String Resource without args
                messageToBeShown = getString(messageId);
            }

            if (!TextUtils.isEmpty(messageToBeShown)) {
                //Displaying the Snackbar message of indefinite time length
                //when we have the error message to be shown

                new UtlitySnack(Snackbar.make(getView(), messageToBeShown, Snackbar.LENGTH_INDEFINITE))
                        .revealCompleteMessage() //Removes the limit on max lines
                        .setDismissAction(R.string.action_snackbar) //For the Dismiss "OK" action
                        .showSnack();
            }
        }
    }


    @Override
    public void showNoLinkError() {
        showError(R.string.no_link_error);
    }

    @Override
    public void launchWebLink(String webUrl) {
        //Launching the Browser application for the Web Page URL passed
        UtilityIntent.openLink(requireContext(), webUrl);
    }

    @Override
    public void launchMapLocation(String location) {
        //Launching the Map application for the location information passed
        UtilityIntent.openMap(requireContext(), location);
    }

    @Override
    public void dialNumber(String contactNumber) {
        //Launching the Dialer passing in the Contact Number
        UtilityIntent.dialPhoneNumber(requireActivity(), contactNumber);
    }

    @Override
    public void launchShareContact(String contactNumber) {
        //Launching the Share Intent to share the contact number
        UtilityIntent.shareText(requireActivity(), contactNumber, getString(R.string.all_contact_title));
    }

    @Override
    public void launchShareLocation(String location) {
        //Launching the Share Intent to share the location text
        UtilityIntent.shareText(requireActivity(), location, getString(R.string.all_location_title));
    }

    private static class HotelListAdapter extends ListAdapter<HotelsClass, HotelListAdapter.ViewHolder> {

        //Payload Constants used to rebind the "Expanded/Collapsed" state of the list items for the position stored here
        private static final String PAYLOAD_EXPAND_CARD = "Payload.Expand.ItemPosition";
        private static final String PAYLOAD_COLLAPSE_CARD = "Payload.Collapse.ItemPosition";

        private static final DiffUtil.ItemCallback<HotelsClass> DIFF_HOTELS
                = new DiffUtil.ItemCallback<HotelsClass>() {

            @Override
            public boolean areItemsTheSame(HotelsClass oldItem, HotelsClass newItem) {
                //Returning the comparison of the HotelsClass's Id
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(HotelsClass oldItem, HotelsClass newItem) {
                //Returning the comparison of Name
                return oldItem.getName().equals(newItem.getName());
            }
        };
        //Context for reading resources
        private final Context mContext;
        //Listener for the User actions on the HotelsClass List Items
        private final UserActionHotel mActionsListener;
        //Stores the Item Position of the Last expanded card
        private int mLastExpandedItemPosition = RecyclerView.NO_POSITION;
        //The Data of this Adapter that stores a list of Hotels to be displayed
        private ArrayList<HotelsClass> mHotelsClassList;

        HotelListAdapter(Context context, UserActionHotel userActionsListener) {
            super(DIFF_HOTELS);
            mContext = context;
            //Registering the User Actions Listener
            mActionsListener = userActionsListener;
        }

        @NonNull
        @Override
        public HotelListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            //Inflating the item layout 'R.layout.item_hotel_list'
            //Passing False since we are attaching the layout ourselves
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.hotel_list, parent, false);
            //Returning the Instance of ViewHolder for the inflated Item V
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull HotelListAdapter.ViewHolder holder, int position) {
            //Get the data at the position
            HotelsClass hotelsClass = getItem(position);

            //Bind the Views with the data at the position
            holder.bind(position, hotelsClass);

            //When we have an Item V that was last expanded
            if (mLastExpandedItemPosition > RecyclerView.NO_POSITION) {
                if (mLastExpandedItemPosition == position) {
                    //Ensures that the Item V remains expanded when being reused
                    holder.expandItemView();
                } else {
                    //Ensures that the Item V remains collapsed when being reused
                    holder.collapseItemView();
                }
            }
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
            if (payloads.isEmpty()) {
                //Propagating to super when there are no payloads
                super.onBindViewHolder(holder, position, payloads);
            } else {
                //When we have a payload for partial update

                //Get the Payload bundle
                Bundle bundle = (Bundle) payloads.get(0);
                //Iterate over the bundle keys
                for (String keyStr : bundle.keySet()) {
                    switch (keyStr) {
                        case PAYLOAD_EXPAND_CARD:
                            //For the Item to be expanded

                            //Get the position from the bundle
                            int itemPositionToExpand = bundle.getInt(keyStr, RecyclerView.NO_POSITION);
                            if (itemPositionToExpand > RecyclerView.NO_POSITION
                                    && itemPositionToExpand == mLastExpandedItemPosition) {
                                //When the position is for the Item to be expanded, expand the Item V
                                holder.expandItemView();
                            }
                            break;
                        case PAYLOAD_COLLAPSE_CARD:
                            //For the Item to be collapsed

                            //Get the position from the bundle
                            int itemPositionToCollapse = bundle.getInt(keyStr, RecyclerView.NO_POSITION);
                            if (itemPositionToCollapse > RecyclerView.NO_POSITION) {
                                //When the Item V Position is valid, collapse the Item V
                                holder.collapseItemView();
                            }
                            break;
                    }
                }
            }
        }

        @Override
        public void submitList(List<HotelsClass> list) {
            //Preparing the list to store a copy of the Adapter data
            if (mHotelsClassList == null) {
                //Initializing the list when not initialized
                mHotelsClassList = new ArrayList<>();
            } else if (mHotelsClassList.size() > 0) {
                //Clearing the list if it has content already
                mHotelsClassList.clear();
            }
            //Adding all the contents of the list submitted
            mHotelsClassList.addAll(list);
            //Propagating the list to super
            super.submitList(list);
        }

        ArrayList<HotelsClass> getHotelList() {
            return mHotelsClassList;
        }

        void changeItemExpanded(int position) {
            //Collapse any previously expanded card
            if (mLastExpandedItemPosition > RecyclerView.NO_POSITION) {
                setItemCollapsed(mLastExpandedItemPosition);
            }
            //Creating a Bundle to do a partial update for expanding the card
            Bundle payloadBundle = new Bundle(1);
            //Put the position of the item into the bundle for update
            payloadBundle.putInt(PAYLOAD_EXPAND_CARD, position);
            //Store the position of the item being expanded for tracking
            mLastExpandedItemPosition = position;
            //Notify the state change at the item position, to expandItemView
            notifyItemChanged(position, payloadBundle);
        }

        void setItemCollapsed(int position) {
            //Creating a Bundle to do a partial update for collapsing the card
            Bundle payloadBundle = new Bundle(1);
            //Put the position of the item into the bundle for update
            payloadBundle.putInt(PAYLOAD_COLLAPSE_CARD, position);
            //Notify the state change at the item position, to collapse
            notifyItemChanged(position, payloadBundle);
            //Reset the last expanded position if the same item is being collapsed
            if (mLastExpandedItemPosition == position) {
                mLastExpandedItemPosition = RecyclerView.NO_POSITION;
            }
        }

        public class ViewHolder extends RecyclerView.ViewHolder
                implements View.OnClickListener, View.OnLongClickListener {
            //References to the Views required, in the Item V
            private final TextView mTextViewHotelName;
            private final ImageView mImageViewHotelStar;
            private final TextView mTextViewTravellerRating;
            private final RatingBar mRatingBar;
            private final ImageView mImageViewHotelPhoto;
            private final TextView mTextViewCost;
            private final TextView mTextViewHotelContact;
            private final TextView mTextViewHotelLocation;
            private final View mViewTopRibbon;
            private final Button mButtonExpandCollapse;
            private final ImageButton mImageButtonLocation;
            private final ImageButton mImageButtonContact;
            private final Group mGroupContactLocationInfo;
            private final Group mGroupActionImageButtons;

            ViewHolder(View itemView) {
                super(itemView);

                //Finding the Views needed
                mTextViewHotelName = itemView.findViewById(R.id.text_hotel_list_item_title);
                mImageViewHotelStar = itemView.findViewById(R.id.image_hotel_list_item_star_type);
                mTextViewTravellerRating = itemView.findViewById(R.id.text_hotel_list_item_rating_value);
                mRatingBar = itemView.findViewById(R.id.ratingbar_hotel_list_item_rating);
                mImageViewHotelPhoto = itemView.findViewById(R.id.image_hotel_list_item_photo);
                mTextViewCost = itemView.findViewById(R.id.text_hotel_list_item_cost);
                mTextViewHotelContact = itemView.findViewById(R.id.text_hotel_list_item_contact);
                mTextViewHotelLocation = itemView.findViewById(R.id.text_hotel_list_item_location);
                mViewTopRibbon = itemView.findViewById(R.id.view_hotel_list_item_top_ribbon);
                mButtonExpandCollapse = itemView.findViewById(R.id.btn_hotel_list_item_expand_collapse);
                mImageButtonContact = itemView.findViewById(R.id.imgbtn_hotel_list_item_contact);
                mImageButtonLocation = itemView.findViewById(R.id.imgbtn_hotel_list_item_location);
                mGroupContactLocationInfo = itemView.findViewById(R.id.group_hotel_list_item_contact_location);
                mGroupActionImageButtons = itemView.findViewById(R.id.group_hotel_list_item_imgbtn_actions);

                //Registering the Click listeners on the required views
                mButtonExpandCollapse.setOnClickListener(this);
                mImageButtonLocation.setOnClickListener(this);
                mTextViewHotelLocation.setOnClickListener(this);
                mTextViewHotelLocation.setOnLongClickListener(this);
                mImageButtonContact.setOnClickListener(this);
                mTextViewHotelContact.setOnClickListener(this);
                mTextViewHotelContact.setOnLongClickListener(this);
                itemView.setOnClickListener(this);
            }

            void bind(int position, HotelsClass hotelsClass) {
                //Bind the HotelsClass Name
                mTextViewHotelName.setText(hotelsClass.getName());
                //Bind the HotelsClass Rating Image if available
                bindImageView(mImageViewHotelStar, hotelsClass.getStarTypeResId());
                //Bind the Traveller's Rating value
                mTextViewTravellerRating.setText(String.valueOf(hotelsClass.getRating()));
                //Bind the RatingBar with the Rating Value
                mRatingBar.setRating(hotelsClass.getRating());
                //Bind the HotelsClass Photo if available
                DecoderImaFrag.newInstance(((FragmentActivity) mContext).getSupportFragmentManager(), position)
                        .executeAndUpdate(mImageViewHotelPhoto, hotelsClass.getPhotoResId());
                //Bind the Cost of Stay
                mTextViewCost.setText(hotelsClass.getCostOfStay());

                //Read and Bind the Contact Number: START
                String contactNumber = hotelsClass.getContactNumber();
                if (!TextUtils.isEmpty(contactNumber)) {
                    //Bind the Contact Info with Phone Number Formatting when present
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mTextViewHotelContact.setText(PhoneNumberUtils.formatNumber(contactNumber, Locale.getDefault().getCountry()));
                    } else {
                        mTextViewHotelContact.setText(PhoneNumberUtils.formatNumber(contactNumber));
                    }
                    //Ensure that the corresponding ImageButton is visible
                    mImageButtonContact.setVisibility(View.VISIBLE);
                } else {
                    //Set a message when No Contact Number is available
                    mTextViewHotelContact.setText(mContext.getString(R.string.all_error_link));
                    //Hide the corresponding ImageButton
                    mImageButtonContact.setVisibility(View.GONE);
                }
                //Read and Bind the Contact Number: END

                //Bind the Location Info
                mTextViewHotelLocation.setText(hotelsClass.getLocation());

                //Bind the Colors using the Palette if derived
                if (hotelsClass.isSwatchGenerated()) {
                    int colorPrimary = ContextCompat.getColor(mContext, R.color.colorPrimary);
                    mViewTopRibbon.setBackgroundColor(colorPrimary);

                } else {
                    //When we do not have any Palette, use the default colors
                    int colorPrimary = ContextCompat.getColor(mContext, R.color.colorPrimary);
                    mViewTopRibbon.setBackgroundColor(colorPrimary);
                    mButtonExpandCollapse.setTextColor(colorPrimary);
                }
            }

            @SuppressLint("ResourceType")
            void bindImageView(ImageView imageView, @DrawableRes int imageResourceId) {
                if (imageResourceId > ImageUtility.NO_RESOURCE_ID) {
                    //When the Image is available

                    //Ensure the ImageView is visible
                    imageView.setVisibility(View.VISIBLE);
                    //Executing on the background thread
                    Executors.newSingleThreadExecutor().execute(() -> {
                        //Decode the Image from the Resource
                        Drawable decodedDrawable = AppCompatResources.getDrawable(mContext, imageResourceId);
                        //Executing on the UI thread
                        ((FragmentActivity) mContext).runOnUiThread(() -> {
                            //Set the Image decoded to the ImageView
                            imageView.setImageDrawable(decodedDrawable);
                        });
                    });
                } else {
                    //Hide the ImageView when the Image is NOT available
                    imageView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onClick(View view) {
                //Checking if the adapter position is valid
                int adapterPosition = getAdapterPosition();
                if (adapterPosition > RecyclerView.NO_POSITION) {
                    //When the adapter position is valid

                    //Get the data at the position
                    HotelsClass hotelsClass = getItem(adapterPosition);

                    //Get the V Id clicked
                    int clickedViewId = view.getId();

                    //Taking action based on the view clicked
                    if (clickedViewId == itemView.getId()) {
                        //When the entire Item V is clicked

                        //Dispatch the event to the action listener
                        mActionsListener.onOpenLink(adapterPosition, hotelsClass);

                    } else if (clickedViewId == mButtonExpandCollapse.getId()) {
                        //When the "Expand/Collapse" button is clicked

                        if (mLastExpandedItemPosition == adapterPosition) {
                            //When the same Item V was previously expanded, collapse the Item V
                            setItemCollapsed(adapterPosition);
                        } else {
                            //Else, expand the Item V
                            changeItemExpanded(adapterPosition);
                        }

                    } else if (clickedViewId == mImageButtonLocation.getId()
                            || clickedViewId == mTextViewHotelLocation.getId()) {
                        //When the Location ImageButton or the Location Info is clicked

                        //Dispatch the event to the action listener
                        mActionsListener.onOpenLocation(adapterPosition, hotelsClass);

                    } else if (clickedViewId == mImageButtonContact.getId()
                            || clickedViewId == mTextViewHotelContact.getId()) {
                        //When the Contact ImageButton or the Contact Info is clicked

                        //Dispatch the event to the action listener
                        mActionsListener.onOpenContact(adapterPosition, hotelsClass);

                    }
                }
            }

            @Override
            public boolean onLongClick(View view) {
                //Checking if the adapter position is valid
                int adapterPosition = getAdapterPosition();
                if (adapterPosition > RecyclerView.NO_POSITION) {
                    //Get the data at the position
                    HotelsClass hotelsClass = getItem(adapterPosition);

                    //Get the V Id clicked
                    int clickedViewId = view.getId();

                    //Taking action based on the view clicked
                    if (clickedViewId == mTextViewHotelContact.getId()) {
                        //When the Contact Info is long pressed

                        //Dispatch the event to the action listener
                        mActionsListener.onContactLongClicked(adapterPosition, hotelsClass);

                        //Returning True to indicate the event was consumed
                        return true;

                    } else if (clickedViewId == mTextViewHotelLocation.getId()) {
                        //When the Location Info is long pressed

                        //Dispatch the event to the action listener
                        mActionsListener.onLocationLongClicked(adapterPosition, hotelsClass);

                        //Returning True to indicate the event was consumed
                        return true;

                    }
                }
                //Returning False when no event was consumed
                return false;
            }

            void expandItemView() {
                //Setting the Button Text to Collapse
                mButtonExpandCollapse.setText(R.string.collapse_action);
                //Revealing the Contact and Location Info
                mGroupContactLocationInfo.setVisibility(View.VISIBLE);
                //Hiding the Contact and Location Action Buttons
                mGroupActionImageButtons.setVisibility(View.GONE);
            }

            void collapseItemView() {
                //Setting the Button Text to Expand
                mButtonExpandCollapse.setText(R.string.expand_action);
                //Hiding the Contact and Location Info
                mGroupContactLocationInfo.setVisibility(View.GONE);
                //Revealing the Contact and Location Action Buttons
                mGroupActionImageButtons.setVisibility(View.VISIBLE);
                //Hide the ImageButton for Contact when no Contact is available
                if (mTextViewHotelContact.getText().toString().equals(mContext.getString(R.string.all_error_link))) {
                    mImageButtonContact.setVisibility(View.GONE);
                }
            }
        }
    }

    private class ItemUserActionHotel implements UserActionHotel {

        @Override
        public void onOpenLink(int itemPosition, HotelsClass hotelsClass) {
            //Delegating to the PresenterBase to handle the event
            mPresenter.openLink(hotelsClass.getWebsite());
        }

        @Override
        public void onOpenLocation(int itemPosition, HotelsClass hotelsClass) {
            //Delegating to the PresenterBase to handle the event
            mPresenter.openLocation(hotelsClass.getLocation());
        }

        @Override
        public void onOpenContact(int itemPosition, HotelsClass hotelsClass) {
            //Delegating to the PresenterBase to handle the event
            mPresenter.openContact(hotelsClass.getContactNumber());
        }

        @Override
        public void onContactLongClicked(int itemPosition, HotelsClass hotelsClass) {
            //Delegating to the PresenterBase to handle the event
            mPresenter.shareContact(hotelsClass.getContactNumber());
        }

        @Override
        public void onLocationLongClicked(int itemPosition, HotelsClass hotelsClass) {
            //Delegating to the PresenterBase to handle the event
            mPresenter.shareLocation(hotelsClass.getLocation());
        }
    }
}
