import math
import struct
import zlib
from pathlib import Path

# The model UVs are baked against 64×64, so a 256×256 image gives four texels per model pixel.
SCALE = 4
WIDTH = 64 * SCALE
HEIGHT = 64 * SCALE
OUT = Path("src/main/resources/assets/ascendant/textures/entity/archer_cloak.png")

WOOL = (92, 78, 50, 255)
WOOL_DARK = (58, 46, 30, 255)
CREASE = (42, 34, 22, 255)
HEM = (36, 28, 18, 255)
LINING = (150, 134, 104, 255)
LINING_DARK = (122, 108, 82, 255)
LEATHER = (104, 70, 44, 255)
FLAP = (72, 46, 28, 255)
BRONZE = (118, 84, 48, 255)
BRONZE_LIGHT = (168, 128, 74, 255)

pixels = bytearray(WIDTH * HEIGHT * 4)


def hash2(x, y):
    n = (int(x) * 374761393 + int(y) * 668265263) & 0xFFFFFFFF
    n = ((n ^ (n >> 13)) * 1274126177) & 0xFFFFFFFF
    return (n ^ (n >> 16)) & 255


def clamp(value):
    return max(0, min(255, int(value)))


def tint(color, delta):
    return (clamp(color[0] + delta), clamp(color[1] + delta), clamp(color[2] + delta), color[3])


def grain(x, y, amount):
    return (hash2(x, y) - 128) * amount // 128


def put(x, y, color):
    if 0 <= x < WIDTH and 0 <= y < HEIGHT:
        i = (y * WIDTH + x) * 4
        pixels[i : i + 4] = bytes(color)


def paint(x, y, w, h, color_at):
    x, y, w, h = int(x), int(y), int(w), int(h)
    for py in range(y, y + h):
        for px in range(x, x + w):
            put(px, py, color_at(px, py, px - x, py - y, w, h))


def wool_outer(seam):
    def color(px, py, lx, ly, w, h):
        fold = math.sin((lx / SCALE) * 1.7) * 12 + math.sin((ly / SCALE) * 0.85) * 6
        color = tint(WOOL, int(fold) + grain(px, py, 14))
        if seam and abs(lx - w / 2) < SCALE * 0.22 and ly % 3 == 0:
            color = CREASE
        if h > SCALE * 3 and ly >= h - SCALE:
            color = tint(HEM, grain(px, py, 6))
        if lx == 0 or lx == w - 1 or ly == 0:
            color = WOOL_DARK
        return color

    return color


def lining_face():
    def color(px, py, lx, ly, w, h):
        shift = math.sin((lx / SCALE) * 2.1) * 6
        color = tint(LINING, int(shift) + grain(px, py, 8))
        if lx == 0 or lx == w - 1 or ly == h - 1:
            color = LINING_DARK
        return color

    return color


def edge_color(px, py, lx, ly, w, h):
    return tint(WOOL_DARK, grain(px, py, 8))


def paint_wool(u, v, w, h, d, seam=False):
    s = SCALE
    u, v, w, h, d = (u * s, v * s, w * s, h * s, d * s)
    paint(u + d, v, w, d, wool_outer(False))
    paint(u + d + w, v, w, d, lambda *args: tint(HEM, grain(args[0], args[1], 6)))
    paint(u, v + d, d, h, edge_color)
    paint(u + d, v + d, w, h, lining_face())
    paint(u + d + w, v + d, d, h, edge_color)
    paint(u + d + w + d, v + d, w, h, wool_outer(seam))


def paint_leather(u, v, w, h, d):
    s = SCALE
    u, v, w, h, d = (u * s, v * s, w * s, h * s, d * s)

    def leather(px, py, lx, ly, fw, fh):
        color = tint(LEATHER, grain(px, py, 16))
        inset = lx < 2 or ly < 2 or lx >= fw - 2 or ly >= fh - 2
        if inset and fh > 6:
            color = FLAP
        return color

    def flap(px, py, lx, ly, fw, fh):
        return tint(FLAP, grain(px, py, 10))

    paint(u + d, v, w, d, flap)
    paint(u + d + w, v, w, d, leather)
    paint(u, v + d, d, h, leather)
    paint(u + d, v + d, w, h, leather)
    paint(u + d + w, v + d, d, h, leather)
    paint(u + d + w + d, v + d, w, h, leather)


def paint_bronze(u, v, w, h, d):
    s = SCALE
    u, v, w, h, d = (u * s, v * s, w * s, h * s, d * s)

    def metal(px, py, lx, ly, fw, fh):
        dx = lx - (fw - 1) / 2
        dy = ly - (fh - 1) / 2
        if dx * dx + dy * dy <= 1.2:
            return BRONZE_LIGHT
        return tint(BRONZE, grain(px, py, 8))

    span_w = d + w + d + w
    span_h = d + h
    paint(u, v, span_w, span_h, metal)


paint_wool(0, 0, 12, 6, 1, True)
paint_wool(44, 0, 1, 7, 5)
paint_wool(44, 14, 1, 5, 2)
paint_bronze(54, 14, 1, 1, 1)
paint_leather(54, 18, 2, 3, 1)
paint_wool(0, 10, 8, 12, 1, True)
paint_wool(20, 10, 4, 11, 1)
paint_wool(32, 10, 4, 10, 1)
paint_wool(0, 24, 10, 12, 1, True)
paint_wool(0, 40, 10, 1, 10)
paint_wool(26, 24, 1, 9, 7)
paint_wool(46, 24, 1, 9, 7)

raw = b"".join(b"\x00" + bytes(pixels[y * WIDTH * 4 : (y + 1) * WIDTH * 4]) for y in range(HEIGHT))


def chunk(tag, data):
    return struct.pack(">I", len(data)) + tag + data + struct.pack(">I", zlib.crc32(tag + data) & 0xFFFFFFFF)


png = (
    b"\x89PNG\r\n\x1a\n"
    + chunk(b"IHDR", struct.pack(">IIBBBBB", WIDTH, HEIGHT, 8, 6, 0, 0, 0))
    + chunk(b"IDAT", zlib.compress(raw, 9))
    + chunk(b"IEND", b"")
)
OUT.parent.mkdir(parents=True, exist_ok=True)
OUT.write_bytes(png)
print(OUT, WIDTH, HEIGHT, len(png))
