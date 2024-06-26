package com.tricakrawala.batikpedia.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tricakrawala.batikpedia.R
import com.tricakrawala.batikpedia.model.Nusantara
import com.tricakrawala.batikpedia.model.Rekomendasi
import com.tricakrawala.batikpedia.navigation.Screen
import com.tricakrawala.batikpedia.ui.common.UiState
import com.tricakrawala.batikpedia.ui.components.CardBerita
import com.tricakrawala.batikpedia.ui.components.NavbarHome
import com.tricakrawala.batikpedia.ui.components.NusantaraItemRow
import com.tricakrawala.batikpedia.ui.theme.BatikPediaTheme
import com.tricakrawala.batikpedia.ui.theme.background2
import com.tricakrawala.batikpedia.ui.theme.primary
import org.koin.androidx.compose.koinViewModel


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navigateToDetail: () -> Unit,
    viewModel: HomeViewModel = koinViewModel(),
    navController: NavHostController,
) {
    val uiStateNusantara by viewModel.uiStateNusantara.collectAsState(initial = UiState.Loading)
    val uiStateRekomendasi by viewModel.uiStateRekomendasi.collectAsState(initial = UiState.Loading)

    LaunchedEffect(true) {
        if (uiStateNusantara is UiState.Loading) {
            viewModel.getAllNusantara()
        }
        if (uiStateRekomendasi is UiState.Loading) {
            viewModel.getAllRekomendasi()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        when (val nusantaraState = uiStateNusantara) {
            is UiState.Success -> {
                val listNusantara = nusantaraState.data
                when (val rekomendasiState = uiStateRekomendasi) {
                    is UiState.Success -> {
                        val listRekomendasi = rekomendasiState.data
                        HomeContent(
                            navigateToDetail = navigateToDetail,
                            listNusantara = listNusantara,
                            listRekomendasi = listRekomendasi,
                            modifier = Modifier.fillMaxSize(),
                            navController = navController
                        )
                    }

                    is UiState.Error -> {}

                    else -> {}
                }
            }

            is UiState.Error -> {}

            else -> {}
        }
    }
}



@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    navigateToDetail: () -> Unit,
    listNusantara: List<Nusantara>,
    listRekomendasi: List<Rekomendasi>,
    navController: NavHostController,
) {

    Box(
        modifier = Modifier
            .background(background2)
            .fillMaxSize()
            .statusBarsPadding()

    ) {
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_logo_batik_pedia),
            contentDescription = "logo batik pedia",
            modifier = Modifier
                .padding(start = 24.dp, top = 36.dp)
        )
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_teks_logo_batik_pedia),
            contentDescription = "logo batik pedia",
            modifier = Modifier
                .padding(start = 68.dp, top = 42.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.ornamen_batik_beranda),
            contentDescription = "",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(180.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 88.dp, start = 24.dp, end = 24.dp)


        ) {

            Box(
                modifier = modifier
                    .clip(RoundedCornerShape(16.dp))
                    .fillMaxWidth()
                    .background(primary)
                    .align(Alignment.CenterHorizontally)
                    .clickable { navController.navigate(Screen.Berita.route) }


            ) {
                CardBerita(
                    image = R.drawable.fake_berita_image,
                    text = "Melihat proses pembuatan batik tulis dan cap di kauman solo",
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(horizontal = 12.dp, vertical = 16.dp)
                )
            }
            NavbarHome(textContent = stringResource(id = R.string.jelajahi_nusantara), modifier = Modifier.clickable { navController.navigate(Screen.ToListProvinsi.route)})

            LazyRow {
                items(listNusantara) { data ->
                    NusantaraItemRow(
                        provinsi = data.provinsi,
                        image = data.image,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
            }

            NavbarHome(textContent = stringResource(id = R.string.rekomendasi_untuk_anda))

            LazyVerticalGrid(
                columns = GridCells.Fixed(count =2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .height(300.dp)
            ) {
                items(listRekomendasi) { data ->
                    Image(
                        painter = painterResource(id = data.image),
                        contentDescription = "Rekomendasi",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .height(150.dp)
                            .clip(RoundedCornerShape(16.dp))

                    )
                }

            }

        }
    }

}

@Preview
@Composable
private fun preview() {

    val fakeNusantaraList = listOf(
        Nusantara(1, R.drawable.yogyakarta,"Provinsi 1",1,1),
        Nusantara(2, R.drawable.yogyakarta,"Provinsi 2",2,2),
    )

    val fakeRekomendasiList = listOf(
        Rekomendasi(1,R.drawable.rekomendasi1),
        Rekomendasi(2,R.drawable.rekomendasi2),
        Rekomendasi(2,R.drawable.rekomendasi2),
        Rekomendasi(2,R.drawable.rekomendasi2),
    )

    BatikPediaTheme {
        HomeContent(navigateToDetail = {  }, listNusantara = fakeNusantaraList, listRekomendasi =fakeRekomendasiList, navController = rememberNavController() )
    }
}